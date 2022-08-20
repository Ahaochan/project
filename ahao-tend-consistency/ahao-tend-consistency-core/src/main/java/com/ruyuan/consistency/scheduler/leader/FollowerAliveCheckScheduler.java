package com.ruyuan.consistency.scheduler.leader;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * leader检测follower是否存活的调度器
 *
 * @author zhonghuashishan
 **/
public class FollowerAliveCheckScheduler {

    /**
     * 获取调度的future
     */
    private final ScheduledFuture<?> scheduledFuture;

    public FollowerAliveCheckScheduler(ScheduledFuture<?> scheduledFuture) {
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
        if (this.scheduledFuture.isCancelled()) {
            return "FollowerAliveCheckScheduler已经被取消了";
        }

        if (this.scheduledFuture.isDone()) {
            return "FollowerAliveCheckScheduler已经调度完成了";
        }

        return "FollowerAliveCheckScheduler还没有执行, 将于" + scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + "ms 后执行";
    }
}
