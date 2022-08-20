package com.ruyuan.consistency.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 一致性框架任务调度器 leader follower都会使用
 *
 * @author zhonghuashishan
 **/
public class ConsistencyTaskScheduler {

    /**
     * 调度的future
     */
    private final ScheduledFuture<?> scheduledFuture;

    public ConsistencyTaskScheduler(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    /**
     * 取消
     */
    public void cancel() {
        this.scheduledFuture.cancel(false);
    }

    @Override
    public String toString() {
        return "ConsistencyTaskTimer{" +
                "scheduledFuture=" + scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + " ms" +
                '}';
    }

}
