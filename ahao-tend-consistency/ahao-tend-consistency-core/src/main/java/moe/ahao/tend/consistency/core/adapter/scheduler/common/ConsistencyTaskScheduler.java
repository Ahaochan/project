package moe.ahao.tend.consistency.core.adapter.scheduler.common;

/**
 * 一致性框架任务调度器 leader follower都会使用
 **/
public class ConsistencyTaskScheduler extends AbstractSingleScheduler{
    public ConsistencyTaskScheduler(int initDelaySecond, int delaySecond) {
        super("taskScheduler", initDelaySecond, delaySecond);
    }
}
