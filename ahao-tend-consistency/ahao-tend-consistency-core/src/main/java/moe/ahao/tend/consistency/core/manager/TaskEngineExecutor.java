package moe.ahao.tend.consistency.core.manager;

import moe.ahao.tend.consistency.core.adapter.scheduler.common.ConsistencyTaskScheduler;
import moe.ahao.tend.consistency.core.aspect.ConsistencyAspect;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;

/**
 * 任务执行引擎接口
 **/
public interface TaskEngineExecutor {

    /**
     * 在当前线程同步执行一致性任务
     *
     * @see ConsistencyTaskScheduler
     * @param taskInstance 任务实例信息
     */
    void execute(ConsistencyTaskInstance taskInstance);

    /**
     * 根据任务配置, 同步或异步、立刻执行或调度执行
     *
     * @see ConsistencyAspect
     * @param taskInstance 任务实例信息
     */
    void executeSmart(ConsistencyTaskInstance taskInstance);

    /**
     * 当执行任务失败的时候，执行该逻辑
     * @param taskInstance             任务实例
     * @param isOpenLocalStorageMode   任务是否只在本地存储中进行了存储
     * @param ex                       异常信息
     */
    void fallback(ConsistencyTaskInstance taskInstance, boolean isOpenLocalStorageMode, Exception ex);
}
