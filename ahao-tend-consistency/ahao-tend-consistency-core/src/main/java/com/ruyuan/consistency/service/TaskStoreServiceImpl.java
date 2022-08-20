package com.ruyuan.consistency.service;

import cn.hutool.json.JSONUtil;
import com.ruyuan.consistency.config.TendConsistencyConfiguration;
import com.ruyuan.consistency.custom.query.TaskTimeRangeQuery;
import com.ruyuan.consistency.enums.ConsistencyTaskStatusEnum;
import com.ruyuan.consistency.enums.PerformanceEnum;
import com.ruyuan.consistency.enums.ThreadWayEnum;
import com.ruyuan.consistency.exceptions.ConsistencyException;
import com.ruyuan.consistency.localstorage.RocksLocalStorage;
import com.ruyuan.consistency.manager.TaskEngineExecutor;
import com.ruyuan.consistency.mapper.TaskStoreMapper;
import com.ruyuan.consistency.model.ConsistencyTaskInstance;
import com.ruyuan.consistency.utils.ReflectTools;
import com.ruyuan.consistency.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;

/**
 * 任务存储的service实现类
 *
 * @author zhonghuashishan
 **/
@Slf4j
@Service
public class TaskStoreServiceImpl implements TaskStoreService {

    /**
     * 任务存储的mapper组件
     */
    @Autowired
    private TaskStoreMapper taskStoreMapper;
    /**
     * 任务执行线程池
     */
    @Autowired
    private CompletionService<ConsistencyTaskInstance> consistencyTaskPool;
    /**
     * 一致性框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    /**
     * 任务执行器
     */
    @Autowired
    private TaskEngineExecutor taskEngineExecutor;
    /**
     * RocksDB工具类
     */
    @Autowired
    private RocksLocalStorage rocksLocalStorage;

    /**
     * 初始化最终一致性任务实例到数据库
     *
     * @param taskInstance 要存储的最终一致性任务的实例信息
     */
    @Override
    public void initTask(ConsistencyTaskInstance taskInstance) {
        Long result = null;
        // 如果写数据到MySQL失败了，那么可以将数据加入到RocksDB
        try {
            result = taskStoreMapper.initTask(taskInstance);
            log.info("[一致性任务框架] 初始化任务结果为 [{}]", result > 0);
        } catch (Exception e) {
            log.error("[一致性任务框架] 初始化任务到数据库时，发生异常，执行降级逻辑，将任务持久化到RocksDB本地存储中, 任务信息为 {}",
                    JSONUtil.toJsonStr(taskInstance), e);
            // 将数据存储到RocksDB中
            rocksLocalStorage.put(taskInstance);
        }
        // 如果执行模式不是立即执行的任务
        if (!PerformanceEnum.PERFORMANCE_RIGHT_NOW.getCode().equals(taskInstance.getPerformanceWay())) {
            return;
        }

        // 判断当前Action是否包含在事务里面，如果是，等事务提交后，再执行Action
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (synchronizationActive) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            submitTaskInstance(taskInstance);
                        }
                    }
            );
        } else {
            submitTaskInstance(taskInstance);
        }
    }

    /**
     * 根据id获取任务实例信息
     *
     * @param id       任务id
     * @param shardKey 任务分片键
     * @return 任务实例信息
     */
    @Override
    public ConsistencyTaskInstance getTaskByIdAndShardKey(Long id, Long shardKey) {
        return taskStoreMapper.getTaskByIdAndShardKey(id, shardKey);
    }

    /**
     * 获取未完成的任务
     *
     * @return 未完成任务的结果集
     */
    @Override
    public List<ConsistencyTaskInstance> listByUnFinishTask() {
        Date startTime, endTime;
        Long limitTaskCount;
        try {
            // 获取TaskTimeLineQuery实现类
            if (!StringUtils.isEmpty(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName())) {
                // 获取Spring容器中所有对于TaskTimeRangeQuery接口的实现类
                Map<String, TaskTimeRangeQuery> beansOfTypeMap = SpringUtil.getBeansOfType(TaskTimeRangeQuery.class);
                TaskTimeRangeQuery taskTimeRangeQuery = getTaskTimeLineQuery(beansOfTypeMap);
                startTime = taskTimeRangeQuery.getStartTime();
                endTime = taskTimeRangeQuery.getEndTime();
                limitTaskCount = taskTimeRangeQuery.limitTaskCount();
                return taskStoreMapper.listByUnFinishTask(startTime.getTime(), endTime.getTime(), limitTaskCount);
            } else {
                startTime = TaskTimeRangeQuery.defaultGetStartTime();
                endTime = TaskTimeRangeQuery.defaultGetEndTime();
                limitTaskCount = TaskTimeRangeQuery.defaultLimitTaskCount();
            }
        } catch (Exception e) {
            log.error("[一致性任务框架] 调用业务服务实现具体的告警通知类时，发生异常", e);
            throw new ConsistencyException(e);
        }
        return taskStoreMapper.listByUnFinishTask(startTime.getTime(), endTime.getTime(), limitTaskCount);
    }

    /**
     * 获取TaskTimeRangeQuery的实现类
     *
     * @param beansOfTypeMap TaskTimeRangeQuery接口实现类的map集合
     * @return 获取TaskTimeRangeQuery的实现类
     */
    private TaskTimeRangeQuery getTaskTimeLineQuery(Map<String, TaskTimeRangeQuery> beansOfTypeMap) {
        // 如果只有一个实现类
        if (beansOfTypeMap.size() == 1) {
            String[] beanNamesForType = SpringUtil.getBeanNamesForType(TaskTimeRangeQuery.class);
            return (TaskTimeRangeQuery) SpringUtil.getBean(beanNamesForType[0]);
        }

        Class<?> clazz = ReflectTools.getClassByName(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName());
        return (TaskTimeRangeQuery) SpringUtil.getBean(clazz);
    }

    /**
     * 启动任务
     *
     * @param consistencyTaskInstance 任务实例信息
     * @return 启动任务的结果
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public int turnOnTask(ConsistencyTaskInstance consistencyTaskInstance) {
        consistencyTaskInstance.setExecuteTime(System.currentTimeMillis());
        consistencyTaskInstance.setTaskStatus(ConsistencyTaskStatusEnum.START.getCode());
        return taskStoreMapper.turnOnTask(consistencyTaskInstance);
    }

    /**
     * 标记任务成功
     *
     * @param consistencyTaskInstance 任务实例信息
     * @return 标记结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int markSuccess(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markSuccess(consistencyTaskInstance);
    }

    /**
     * 标记任务为失败
     *
     * @param consistencyTaskInstance 一致性任务信息
     * @return 标记结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int markFail(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markFail(consistencyTaskInstance);
    }

    /**
     * 标记为降级失败
     *
     * @param consistencyTaskInstance 一致性任务实例
     * @return 标记结果
     */
    @Override
    public int markFallbackFail(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markFallbackFail(consistencyTaskInstance);
    }

    /**
     * 提交任务
     *
     * @param taskInstance 任务实例
     */
    @Override
    public void submitTaskInstance(ConsistencyTaskInstance taskInstance) {
        if (ThreadWayEnum.SYNC.getCode().equals(taskInstance.getThreadWay())) {
            // 选择事务事务模型并执行任务
            taskEngineExecutor.executeTaskInstance(taskInstance);
        } else if (ThreadWayEnum.ASYNC.getCode().equals(taskInstance.getThreadWay())) {
            consistencyTaskPool.submit(() -> {
                taskEngineExecutor.executeTaskInstance(taskInstance);
                return taskInstance;
            });
        }
    }

}
