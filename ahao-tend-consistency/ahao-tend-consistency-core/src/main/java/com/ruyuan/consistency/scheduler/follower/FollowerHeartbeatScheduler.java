package com.ruyuan.consistency.scheduler.follower;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * follower对leader发送心跳使用的调度器
 *
 * @author zhonghuashishan
 **/
public class FollowerHeartbeatScheduler {

    /**
     * 获取调度的future
     */
    private final ScheduledFuture<?> scheduledFuture;

    public FollowerHeartbeatScheduler(ScheduledFuture<?> scheduledFuture) {
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
            return "FollowerHeartbeatScheduler已经被取消了";
        }

        if (this.scheduledFuture.isDone()) {
            return "FollowerHeartbeatScheduler已经调度完成了";
        }

        return "FollowerHeartbeatScheduler还没有执行, 将于" + scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + "ms 后执行";
    }
}
