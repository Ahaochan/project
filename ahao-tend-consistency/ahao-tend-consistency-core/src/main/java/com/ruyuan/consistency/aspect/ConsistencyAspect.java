package com.ruyuan.consistency.aspect;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.ruyuan.consistency.annotation.ConsistencyTask;
import com.ruyuan.consistency.config.TendConsistencyConfiguration;
import com.ruyuan.consistency.custom.shard.SnowflakeShardingKeyGenerator;
import com.ruyuan.consistency.election.PeerElectionHandler;
import com.ruyuan.consistency.enums.ConsistencyTaskStatusEnum;
import com.ruyuan.consistency.enums.PerformanceEnum;
import com.ruyuan.consistency.model.ConsistencyTaskInstance;
import com.ruyuan.consistency.service.TaskStoreService;
import com.ruyuan.consistency.utils.ReflectTools;
import com.ruyuan.consistency.utils.ThreadLocalUtil;
import com.ruyuan.consistency.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 一致性事务框架切面
 *
 * @author zhonghuashishan
 */
@Slf4j
@Aspect
@Component
public class ConsistencyAspect {

    /**
     * 缓存生成任务分片key的对象实例
     */
    private static Object cacheGenerateShardKeyClassInstance = null;
    /**
     * 缓存生成任务分片key的方法
     */
    private static Method cacheGenerateShardKeyMethod = null;
    /**
     * 雪花算法workId
     */
    private static String workId;

    /**
     * 一致性任务的service
     */
    @Autowired
    private TaskStoreService taskStoreService;
    /**
     * 框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    /**
     * 选举处理器
     */
    @Autowired
    private PeerElectionHandler peerElectionHandler;

    /**
     * 标注了ConsistencyTask的注解的方法执行前要做的工作
     *
     * @param point 切面信息
     */
    @Around("@annotation(consistencyTask)")
    public Object markConsistencyTask(ProceedingJoinPoint point, ConsistencyTask consistencyTask) throws Throwable {
        log.info("access method:{} is called on {} args {}", point.getSignature().getName(), point.getThis(), point.getArgs());

        // 是否是调度器在执行任务，如果是则直接执行任务即可，因为之前已经进行了任务持久化
        if (ThreadLocalUtil.getFlag()) {
            return point.proceed();
        }

        // 根据注解构造构造最终一致性任务的实例
        ConsistencyTaskInstance taskInstance = createTaskInstance(consistencyTask, point);

        // 初始化任务数据到数据库
        taskStoreService.initTask(taskInstance);

        // 无论是调度执行还是立即执行的任务，任务初始化完成后不对目标方法进行访问，因此返回null
        return null;
    }

    /**
     * 根据注解构造构造最终一致性任务的实例
     *
     * @param task  一致性任务注解信息 相当于任务的模板
     * @param point 方法切入点
     * @return 一致性任务实例
     */
    private ConsistencyTaskInstance createTaskInstance(ConsistencyTask task, JoinPoint point) {
        // 根据入参数组获取对应的Class对象的数组
        Class<?>[] argsClazz = ReflectTools.getArgsClass(point.getArgs());
        // 获取被拦截方法的全限定名称 格式：类路径#方法名(参数1的类型,参数2的类型,...参数N的类型)
        String fullyQualifiedName = ReflectTools.getTargetMethodFullyQualifiedName(point, argsClazz);
        // 获取入参的类名称数组
        String parameterTypes = ReflectTools.getArgsClassNames(point.getSignature());

        Date date = new Date();
        ConsistencyTaskInstance instance = ConsistencyTaskInstance.builder()
                .taskId(StringUtils.isEmpty(task.id()) ? fullyQualifiedName : task.id())
                .methodName(point.getSignature().getName())
                .parameterTypes(parameterTypes)
                .methodSignName(fullyQualifiedName)
                .taskParameter(JSONUtil.toJsonStr(point.getArgs()))
                .performanceWay(task.performanceWay().getCode())
                .threadWay(task.threadWay().getCode())
                .executeIntervalSec(task.executeIntervalSec())
                .delayTime(task.delayTime())
                .executeTimes(0)
                .taskStatus(ConsistencyTaskStatusEnum.INIT.getCode())
                .errorMsg("")
                .alertExpression(StringUtils.isEmpty(task.alertExpression()) ? "" : task.alertExpression())
                .alertActionBeanName(StringUtils.isEmpty(task.alertActionBeanName()) ? "" : task.alertActionBeanName())
                .fallbackClassName(ReflectTools.getFullyQualifiedClassName(task.fallbackClass()))
                .fallbackErrorMsg("")
                .gmtCreate(date)
                .gmtModified(date)
                .build();
        // 设置执行时间
        instance.setExecuteTime(getExecuteTime(instance));
        // 设置分片key
        instance.setShardKey(tendConsistencyConfiguration.getTaskSharded() ? generateShardKey() : 0L);

        return instance;
    }

    /**
     * 获取任务执行时间
     *
     * @param taskInstance 一致性任务实例
     * @return 下次执行时间
     */
    private long getExecuteTime(ConsistencyTaskInstance taskInstance) {
        if (PerformanceEnum.PERFORMANCE_SCHEDULE.getCode().equals(taskInstance.getPerformanceWay())) {
            long delayTimeMillSecond = TimeUtils.secToMill(taskInstance.getDelayTime());
            return System.currentTimeMillis() + delayTimeMillSecond;
        } else {
            return System.currentTimeMillis();
        }
    }

    /**
     * 获取分片键
     *
     * @return 生成分片键
     */
    private Long generateShardKey() {
        SnowflakeShardingKeyGenerator instance = SnowflakeShardingKeyGenerator.getInstance();
        if (!ObjectUtils.isEmpty(peerElectionHandler.getConsistencyTaskShardingContext()) && StringUtils.isEmpty(workId)) {
            if (!ObjectUtils.isEmpty(peerElectionHandler.getConsistencyTaskShardingContext().getCurrentPeerId())) {
                workId = peerElectionHandler.getConsistencyTaskShardingContext().getCurrentPeerId();
                instance.setWorkerId(workId);
            }
        }

        // 如果配置文件中，没有配置自定义任务分片键生成类，则使用框架自带的
        if (StringUtils.isEmpty(tendConsistencyConfiguration.getShardingKeyGeneratorClassName())) {
            return instance.generateShardKey();
        }
        // 如果生成任务CACHE_GENERATE_SHARD_KEY_METHOD的方法存在，就直接调用该方法
        if (!ObjectUtils.isEmpty(cacheGenerateShardKeyMethod)
                && !ObjectUtils.isEmpty(cacheGenerateShardKeyClassInstance)) {
            try {
                return (Long) cacheGenerateShardKeyMethod.invoke(cacheGenerateShardKeyClassInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("使用自定义类生成任务分片键时，发生异常", e);
            }
        }
        // 获取用户自定义的任务分片键的class
        Class<?> shardingKeyGeneratorClass = getUserCustomShardingKeyGenerator();
        if (!ObjectUtils.isEmpty(shardingKeyGeneratorClass)) {
            String methodName = "generateShardKey";
            Method generateShardKeyMethod = ReflectUtil.getMethod(shardingKeyGeneratorClass, methodName);
            try {
                Constructor<?> constructor = ReflectUtil.getConstructor(shardingKeyGeneratorClass);
                cacheGenerateShardKeyClassInstance = constructor.newInstance();
                cacheGenerateShardKeyMethod = generateShardKeyMethod;
                return (Long) cacheGenerateShardKeyMethod.invoke(cacheGenerateShardKeyClassInstance);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error("使用自定义类生成任务分片键时，发生异常", e);
                // 如果指定的自定义分片键生成报错，使用框架自带的
                return instance.generateShardKey();
            }
        }
        return instance.generateShardKey();
    }

    /**
     * 获取ShardingKeyGenerator的实现类
     */
    private Class<?> getUserCustomShardingKeyGenerator() {
        return ReflectTools.getClassByName(tendConsistencyConfiguration.getShardingKeyGeneratorClassName());
    }

}
