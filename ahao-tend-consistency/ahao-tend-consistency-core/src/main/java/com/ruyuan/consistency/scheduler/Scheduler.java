package com.ruyuan.consistency.scheduler;

import com.ruyuan.consistency.scheduler.follower.FollowerHeartbeatScheduler;
import com.ruyuan.consistency.scheduler.follower.LeaderAliveCheckScheduler;
import com.ruyuan.consistency.scheduler.leader.FollowerAliveCheckScheduler;
import com.ruyuan.consistency.scheduler.leader.LeaderToFollowerHeartbeatScheduler;

/**
 * 任务调度器接口
 *
 * @author zhonghuashishan
 **/
public interface Scheduler {

    /**
     * 创建leader用于检测follower是否存活的调度器
     *
     * @param task 任务task
     * @return 选举超时调度器
     */
    FollowerAliveCheckScheduler createFollowerAliveCheckScheduler(Runnable task);

    /**
     * 心跳任务的调度器
     *
     * @param task 心跳任务
     * @return 心跳任务调度器
     */
    LeaderToFollowerHeartbeatScheduler createLeaderToFollowerHeartbeatScheduler(Runnable task);

    /**
     * 任务执行调度器
     *
     * @param task 一致性任务调度器
     * @return 一致性任务调度器
     */
    ConsistencyTaskScheduler createConsistencyTaskScheduler(Runnable task);

    /**
     * 创建follower对leader心跳检测的调度器
     *
     * @param task 心跳任务
     * @return follower心跳任务调度器
     */
    FollowerHeartbeatScheduler createFollowerHeartbeatScheduler(Runnable task);

    /**
     * 创建follower用于检测leader是否存活的调度器
     *
     * @param task 任务
     * @return follower用于检测leader是否存活的调度器
     */
    LeaderAliveCheckScheduler createLeaderAliveScheduler(Runnable task);

    /**
     * 取消所有调度任务
     */
    void cancelAllScheduler();

}
