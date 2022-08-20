package moe.ahao.tend.consistency.core.aspect;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.annotation.ConsistencyTask;
import moe.ahao.tend.consistency.core.infrastructure.repository.TaskStoreRepository;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstanceFactory;
import moe.ahao.tend.consistency.core.manager.TaskEngineExecutor;
import moe.ahao.tend.consistency.core.utils.ThreadLocalUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 一致性事务框架切面
 */
@Slf4j
@Aspect
@Component
public class ConsistencyAspect {
    @Autowired
    private ConsistencyTaskInstanceFactory consistencyTaskInstanceFactory;
    /**
     * 一致性任务的存储
     */
    @Autowired
    private TaskStoreRepository taskStoreRepository;
    /**
     * 任务执行器
     */
    @Autowired
    private TaskEngineExecutor taskEngineExecutor;

    /**
     * 标注了ConsistencyTask的注解的方法执行前要做的工作
     *
     * @param point 切面信息
     */
    @Around("@annotation(consistencyTask)")
    public Object markConsistencyTask(ProceedingJoinPoint point, ConsistencyTask consistencyTask) throws Throwable {
        log.info("access method:{} is called on {} args {}", point.getSignature().getName(), point.getThis(), point.getArgs());

        // 1. 反射的时候也会走AOP, 所以这里要判断一下, 如果是框架内部invoke调用, 就不走AOP逻辑了
        if (ThreadLocalUtil.getFlag()) {
            return point.proceed();
        }

        // 2. 解析@ConsistencyTask中的属性, 构造最终一致性任务的实例
        ConsistencyTaskInstance taskInstance = consistencyTaskInstanceFactory.createInstance(consistencyTask, point);

        // 3. 初始化任务数据到数据库
        taskStoreRepository.store(taskInstance);

        // 4. 无论是调度执行还是立即执行的任务, 任务初始化完成后会通过invoke反射调用, 因此这里返回null
        taskEngineExecutor.executeSmart(taskInstance);
        return null;
    }
}
