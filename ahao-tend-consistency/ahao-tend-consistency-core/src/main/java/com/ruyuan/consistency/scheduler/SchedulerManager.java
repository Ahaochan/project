package com.ruyuan.consistency.scheduler;

import com.ruyuan.consistency.config.TendConsistencyConfiguration;
import com.ruyuan.consistency.scheduler.follower.FollowerHeartbeatScheduler;
import com.ruyuan.consistency.scheduler.follower.LeaderAliveCheckScheduler;
import com.ruyuan.consistency.scheduler.leader.FollowerAliveCheckScheduler;
import com.ruyuan.consistency.scheduler.leader.LeaderToFollowerHeartbeatScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author zhonghuashishan
 **/
@Slf4j
@Component
public class SchedulerManager implements Scheduler {

    /**
     * leader检测follower是否存活的调度器所使用的线程
     */
    private ScheduledExecutorService followerAliveCheckScheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "followerActiveScheduler"));
    /**
     * leader发送给follower心跳调度所用的线程
     */
    private ScheduledExecutorService heartbeatScheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "leaderHeartScheduler"));
    /**
     * 一致性任务调度线程
     */
    private ScheduledExecutorService taskScheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "taskScheduler"));
    /**
     * leader是否存活的线程
     */
    private ScheduledExecutorService leaderAliveScheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "leaderAliveScheduler"));
    /**
     * follower向leader发送的心跳线程
     */
    private ScheduledExecutorService followerHeartbeatScheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "followerHeartbeat"));

    /**
     * leader节点专用 leader定时发给follower的心跳的调度器，同时也会将leader对任务的分片信息发送给各个follower节点
     */
    private LeaderToFollowerHeartbeatScheduler leaderToFollowerHeartbeatScheduler;
    /**
     * leader节点专用 leader检测follower是否存活的调度器
     */
    private FollowerAliveCheckScheduler followerAliveCheckScheduler;
    /**
     * follower专用 follower对leader心跳响应超时检测使用的调度器
     */
    private FollowerHeartbeatScheduler followerHeartbeatTaskScheduler;
    /**
     * follower专用 follower用于检测leader是否存活的调度器
     */
    private LeaderAliveCheckScheduler leaderAliveCheckScheduler;
    /**
     * 一致性任务执行 所使用的调度器，leader和follower都会使用
     */
    private ConsistencyTaskScheduler consistencyTaskScheduler;
    /**
     * 一致性任务框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    /**
     * 创建leader检测follower是否存活的调度器
     *
     * @param task 任务task
     * @return 选举超时调度器
     */
    @Override
    public FollowerAliveCheckScheduler createFollowerAliveCheckScheduler(Runnable task) {
        if (!ObjectUtils.isEmpty(followerAliveCheckScheduler)) {
            followerAliveCheckScheduler.cancel();
        }
        ScheduledFuture<?> scheduledFuture = followerAliveCheckScheduledExecutorService
                .scheduleWithFixedDelay(task, 10, tendConsistencyConfiguration.getFollowerAliveCheckIntervalSeconds(), TimeUnit.SECONDS);
        followerAliveCheckScheduler = new FollowerAliveCheckScheduler(scheduledFuture);
        return followerAliveCheckScheduler;
    }

    /**
     * 创建leader定时发给follower的心跳的调度器，同时也会将leader对任务的分片信息发送给各个follower节点
     *
     * @param task 心跳任务
     * @return 日志复制任务调度器
     */
    @Override
    public LeaderToFollowerHeartbeatScheduler createLeaderToFollowerHeartbeatScheduler(Runnable task) {
        if (!ObjectUtils.isEmpty(leaderToFollowerHeartbeatScheduler)) {
            leaderToFollowerHeartbeatScheduler.cancel();
        }
        // 你作为leader来说，你只要有一个线程不停的调度，不停的发送心跳给所有的follower就可以了
        // leader每隔10s，会去给follower发送一次心跳
        ScheduledFuture<?> scheduledFuture = heartbeatScheduledExecutorService
                .scheduleWithFixedDelay(task, 0, tendConsistencyConfiguration.getLeaderToFollowerHeartbeatIntervalSeconds(), TimeUnit.SECONDS);
        leaderToFollowerHeartbeatScheduler = new LeaderToFollowerHeartbeatScheduler(scheduledFuture);
        return leaderToFollowerHeartbeatScheduler;
    }

    /**
     * 任务执行调度器
     *
     * @param task 一致性任务调度器
     * @return 一致性任务
     */
    @Override
    public ConsistencyTaskScheduler createConsistencyTaskScheduler(Runnable task) {
        if (!ObjectUtils.isEmpty(consistencyTaskScheduler)) {
            consistencyTaskScheduler.cancel();
        }
        ScheduledFuture<?> scheduledFuture = taskScheduledExecutorService
                .scheduleWithFixedDelay(task, 0, tendConsistencyConfiguration.getConsistencyTaskExecuteIntervalSeconds(), TimeUnit.SECONDS);
        consistencyTaskScheduler = new ConsistencyTaskScheduler(scheduledFuture);
        return consistencyTaskScheduler;
    }

    /**
     * 创建follower对leader发送心跳的调度器
     *
     * @param task 心跳任务
     * @return follower心跳任务调度器
     */
    @Override
    public FollowerHeartbeatScheduler createFollowerHeartbeatScheduler(Runnable task) {
        if (!ObjectUtils.isEmpty(followerHeartbeatTaskScheduler)) {
            followerHeartbeatTaskScheduler.cancel();
        }
        ScheduledFuture<?> scheduledFuture = followerHeartbeatScheduledExecutorService
                .scheduleWithFixedDelay(task, 0, tendConsistencyConfiguration.getFollowerHeartbeatIntervalSeconds(), TimeUnit.SECONDS);
        followerHeartbeatTaskScheduler = new FollowerHeartbeatScheduler(scheduledFuture);
        return followerHeartbeatTaskScheduler;
    }

    /**
     * 创建follower用于检测leader是否存活的调度器
     *
     * @param task 任务
     * @return follower用于检测leader是否存活的调度器
     */
    @Override
    public LeaderAliveCheckScheduler createLeaderAliveScheduler(Runnable task) {
        if (!ObjectUtils.isEmpty(leaderAliveCheckScheduler)) {
            leaderAliveCheckScheduler.cancel();
        }
        ScheduledFuture<?> scheduledFuture = leaderAliveScheduledExecutorService
                .scheduleWithFixedDelay(task, 0, tendConsistencyConfiguration.getLeaderAliveCheckIntervalSeconds(), TimeUnit.SECONDS);
        leaderAliveCheckScheduler = new LeaderAliveCheckScheduler(scheduledFuture);
        return leaderAliveCheckScheduler;
    }

    /**
     * 取消所有调度任务
     */
    @Override
    public void cancelAllScheduler() {
        // leader他会不断的给follower发送心跳，follower也会不断地给leader发送心跳
        // leader来说，他需要定时检查follower发送过来的心跳，follower是否宕机
        // follower来说，他需要定时检查leader发送过来的心跳，leader是否宕机
        if (!ObjectUtils.isEmpty(followerAliveCheckScheduler)) {
            followerAliveCheckScheduler.cancel();
        }
        if (!ObjectUtils.isEmpty(leaderToFollowerHeartbeatScheduler)) {
            leaderToFollowerHeartbeatScheduler.cancel();
        }
        if (!ObjectUtils.isEmpty(followerHeartbeatTaskScheduler)) {
            followerHeartbeatTaskScheduler.cancel();
        }
        if (!ObjectUtils.isEmpty(leaderAliveCheckScheduler)) {
            leaderAliveCheckScheduler.cancel();
        }
    }

}
