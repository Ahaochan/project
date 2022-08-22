package moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.annotation.ConsistencyTask;
import moe.ahao.tend.consistency.core.spi.shard.shardkey.SnowflakeShardingKeyGenerator;
import moe.ahao.tend.consistency.core.election.PeerElectionHandler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.infrastructure.enums.ConsistencyTaskStatusEnum;
import moe.ahao.tend.consistency.core.infrastructure.enums.PerformanceEnum;
import moe.ahao.tend.consistency.core.utils.ReflectTools;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.reflect.ClassHelper;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 一致性任务的实例信息
 **/
@Slf4j
@Component
public class ConsistencyTaskInstanceFactory {
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
     * 选举处理器
     */
    @Autowired
    private PeerElectionHandler peerElectionHandler;
    /**
     * 框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    @Autowired
    private PeerNodeManager peerNodeManager;

    /**
     * 根据注解构造构造最终一致性任务的实例
     *
     * @param task  一致性任务注解信息 相当于任务的模板
     * @param point 方法切入点
     * @return 一致性任务实例
     */
    public ConsistencyTaskInstance createInstance(ConsistencyTask task, JoinPoint point) {
        // 根据入参数组获取对应的Class对象的数组
        Class<?>[] argsClazz = ReflectTools.getArgsClass(point.getArgs());
        // 获取被拦截方法的全限定名称 格式：类路径#方法名(参数1的类型,参数2的类型,...参数N的类型)
        String fullyQualifiedName = ReflectTools.getTargetMethodFullyQualifiedName(point, argsClazz);
        // 获取入参的类名称数组
        String parameterTypes = ReflectTools.getArgsClassNames(point.getSignature());

        Date date = new Date();

        ConsistencyTaskInstance instance = new ConsistencyTaskInstance();
        instance.setTaskId(StringUtils.isEmpty(task.id()) ? fullyQualifiedName : task.id());
        instance.setMethodSignName(fullyQualifiedName);
        instance.setMethodName(point.getSignature().getName());
        instance.setParameterTypes(parameterTypes);
        instance.setTaskParameter(JSONHelper.toString(point.getArgs()));
        instance.setTaskStatus(ConsistencyTaskStatusEnum.INIT.getCode());
        instance.setExecuteIntervalSec(task.executeIntervalSec());
        instance.setDelayTime(task.delayTime());
        instance.setExecuteTimes(0);
        // 设置执行时间
        instance.setExecuteTime(getExecuteTime(instance));
        instance.setErrorMsg("");
        instance.setPerformanceWay(task.performanceWay().getCode());
        instance.setThreadWay(task.threadWay().getCode());
        instance.setAlertExpression(StringUtils.isEmpty(task.alertExpression()) ? "" : task.alertExpression());
        instance.setAlertActionBeanName(StringUtils.isEmpty(task.alertActionBeanName()) ? "" : task.alertActionBeanName());
        instance.setFallbackClassName(ReflectTools.getFullyQualifiedClassName(task.fallbackClass()));
        instance.setFallbackErrorMsg("");
        // 设置分片key
        instance.setShardKey(tendConsistencyConfiguration.getTaskSharded() ? generateShardKey() : 0L);
        instance.setGmtCreate(date);
        instance.setGmtModified(date);

        return instance;
    }

    /**
     * 获取任务执行时间
     *
     * @param taskInstance 一致性任务实例
     * @return 下次执行时间
     */
    private long getExecuteTime(ConsistencyTaskInstance taskInstance) {
        if (Objects.equals(PerformanceEnum.SCHEDULE.getCode(), taskInstance.getPerformanceWay())) {
            long delayTimeMillSecond = TimeUnit.SECONDS.toMillis(taskInstance.getDelayTime());
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
        if (StringUtils.isEmpty(workId)) {
            if (!ObjectUtils.isEmpty(peerNodeManager.getSelfPeerNode())) {
                workId = peerNodeManager.getSelfPeerNode().getId().toString();
                instance.setWorkerId(workId);
            }
        }

        // 1. 如果没有配置自定义任务分片key生成类, 就使用自带的snowflake算法生成
        String shardingKeyGeneratorClassName = tendConsistencyConfiguration.getShardingKeyGeneratorClassName();
        if (StringUtils.isEmpty(shardingKeyGeneratorClassName)) {
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
        // 3. 如果之前没有生成过自定义任务分片key, 就反射获取Method
        // 获取用户自定义的任务分片键的class
        Class<?> shardingKeyGeneratorClass = ClassHelper.forName(tendConsistencyConfiguration.getShardingKeyGeneratorClassName());
        if (!ObjectUtils.isEmpty(shardingKeyGeneratorClass)) {
            String methodName = "generateShardKey";
            Method generateShardKeyMethod = ReflectHelper.getMethod(shardingKeyGeneratorClass, methodName);
            try {
                Constructor<?> constructor = shardingKeyGeneratorClass.getConstructor();
                // 3.1. 缓存Method和实例对象
                cacheGenerateShardKeyClassInstance = constructor.newInstance();
                cacheGenerateShardKeyMethod = generateShardKeyMethod;
                // 3.2. 反射调用生成任务分片key
                return (Long) cacheGenerateShardKeyMethod.invoke(cacheGenerateShardKeyClassInstance);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                log.error("使用自定义类生成任务分片键时，发生异常", e);
                // 3.3. 如果指定的自定义分片键生成报错, 就使用自带的snowflake算法生成
                return instance.generateShardKey();
            }
        }
        return instance.generateShardKey();
    }
}
