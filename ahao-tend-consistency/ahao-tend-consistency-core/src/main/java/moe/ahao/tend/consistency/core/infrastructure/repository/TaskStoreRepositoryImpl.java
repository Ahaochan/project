package moe.ahao.tend.consistency.core.infrastructure.repository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.custom.query.TaskTimeRangeQuery;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.infrastructure.enums.ConsistencyTaskStatusEnum;
import moe.ahao.tend.consistency.core.infrastructure.exceptions.ConsistencyException;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.mapper.TaskStoreMapper;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.rocksdb.RocksLocalStorage;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.reflect.ClassHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务存储的service实现类
 **/
@Slf4j
@Service
public class TaskStoreRepositoryImpl implements TaskStoreRepository, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    /**
     * 一致性框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    /**
     * 任务存储的mapper组件
     */
    @Autowired
    private TaskStoreMapper taskStoreMapper;
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
    public void store(ConsistencyTaskInstance taskInstance) {
        try {
            // 1. 持久化到MySQL, 异常就写入RocksDB
            Long result = taskStoreMapper.initTask(taskInstance);
            log.info("[一致性任务框架] 初始化任务结果为 [{}]", result > 0);
        } catch (Exception e) {
            // 2. 将数据存储到RocksDB中
            log.error("[一致性任务框架] 初始化任务到数据库时，发生异常，执行降级逻辑，将任务持久化到RocksDB本地存储中, 任务信息为 {}", JSONHelper.toString(taskInstance), e);
            rocksLocalStorage.put(taskInstance);
        }
    }

    /**
     * 获取未完成的任务
     *
     * @return 未完成任务的结果集
     */
    @Override
    public List<ConsistencyTaskInstance> getUnFinishTaskList() {
        Date startTime, endTime;
        Long limitTaskCount;
        try {
            // 获取TaskTimeLineQuery实现类
            if (StringUtils.isNotEmpty(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName())) {
                // 获取Spring容器中所有对于TaskTimeRangeQuery接口的实现类
                Map<String, TaskTimeRangeQuery> beansOfTypeMap = applicationContext.getBeansOfType(TaskTimeRangeQuery.class);
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
            String[] beanNamesForType = applicationContext.getBeanNamesForType(TaskTimeRangeQuery.class);
            return (TaskTimeRangeQuery) applicationContext.getBean(beanNamesForType[0]);
        }
        Class<?> clazz = ClassHelper.forName(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName());
        return (TaskTimeRangeQuery) applicationContext.getBean(clazz);
    }

    /**
     * 标记任务状态为START, 并记录任务启动时间
     * TODO 开启一个新的事务, 避免异常导致回滚之前的事务
     *
     * @param consistencyTaskInstance 任务实例信息
     * @return 启动任务的结果
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public int markStart(ConsistencyTaskInstance consistencyTaskInstance) {
        consistencyTaskInstance.setExecuteTime(System.currentTimeMillis());
        consistencyTaskInstance.setTaskStatus(ConsistencyTaskStatusEnum.START.getCode());
        return taskStoreMapper.markStart(consistencyTaskInstance);
    }

    /**
     * 标记任务执行成功
     * 实现上是删除数据, 下次定时任务扫描不到这个任务就不会去执行了
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
}
