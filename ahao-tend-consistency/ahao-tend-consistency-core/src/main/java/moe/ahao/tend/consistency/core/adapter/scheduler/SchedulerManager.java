package moe.ahao.tend.consistency.core.adapter.scheduler;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.adapter.scheduler.common.ConsistencyTaskScheduler;
import moe.ahao.tend.consistency.core.adapter.scheduler.follower.FollowerHeartbeatScheduler;
import moe.ahao.tend.consistency.core.adapter.scheduler.follower.LeaderAliveCheckScheduler;
import moe.ahao.tend.consistency.core.adapter.scheduler.leader.FollowerAliveCheckScheduler;
import moe.ahao.tend.consistency.core.adapter.scheduler.leader.LeaderToFollowerHeartbeatScheduler;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.infrastructure.gateway.PeerNodeGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerManager implements Scheduler {
    /**
     * 一致性任务框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    @Autowired
    private PeerNodeGateway peerNodeGateway;

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
     * 创建leader用于检测follower是否存活的调度器
     *
     * @param task 任务task
     */
    @Override
    public void startFollowerAliveCheckScheduler(Runnable task) {
        // leader检测follower是否存活的调度器所使用的线程
        if (followerAliveCheckScheduler == null) {
            followerAliveCheckScheduler = new FollowerAliveCheckScheduler(10, tendConsistencyConfiguration.getFollowerAliveCheckIntervalSeconds());
        }
        followerAliveCheckScheduler.start(task);
    }

    /**
     * 创建leader定时发给follower的心跳的调度器，同时也会将leader对任务的分片信息发送给各个follower节点
     */
    @Override
    public void startLeaderToFollowerHeartbeatScheduler() {
        // 你作为leader来说，你只要有一个线程不停的调度，不停的发送心跳给所有的follower就可以了
        // leader每隔10s，会去给follower发送一次心跳
        // leader发送给follower心跳调度所用的线程
        if (leaderToFollowerHeartbeatScheduler == null) {
            leaderToFollowerHeartbeatScheduler = new LeaderToFollowerHeartbeatScheduler(peerNodeGateway, 0, tendConsistencyConfiguration.getLeaderToFollowerHeartbeatIntervalSeconds());
        }
        leaderToFollowerHeartbeatScheduler.start();
    }

    /**
     * 任务执行调度器
     *
     * @param task 一致性任务调度器
     */
    @Override
    public void createConsistencyTaskScheduler(Runnable task) {
        // 一致性任务调度线程
        if (consistencyTaskScheduler == null) {
            consistencyTaskScheduler = new ConsistencyTaskScheduler(0, tendConsistencyConfiguration.getConsistencyTaskExecuteIntervalSeconds());
        }
        consistencyTaskScheduler.start(task);
    }

    /**
     * 创建follower对leader发送心跳的调度器
     *
     * @param task 心跳任务
     */
    @Override
    public void startFollowerHeartbeatScheduler() {
        // follower向leader发送的心跳线程
        if (followerHeartbeatTaskScheduler == null) {
            followerHeartbeatTaskScheduler = new FollowerHeartbeatScheduler(peerNodeGateway, 0, tendConsistencyConfiguration.getFollowerHeartbeatIntervalSeconds());
        }
        followerHeartbeatTaskScheduler.start();
    }

    /**
     * 创建follower用于检测leader是否存活的调度器
     *
     * @param task 任务
     * @return follower用于检测leader是否存活的调度器
     */
    @Override
    public void startLeaderAliveScheduler(Runnable task) {
        // leader是否存活的线程
        leaderAliveCheckScheduler = new LeaderAliveCheckScheduler(0, tendConsistencyConfiguration.getLeaderAliveCheckIntervalSeconds());
        leaderAliveCheckScheduler.start(task);
    }

    /**
     * 取消所有调度任务
     */
    @Override
    public void cancelAllScheduler() {
        // leader他会不断的给follower发送心跳，follower也会不断地给leader发送心跳
        // leader来说，他需要定时检查follower发送过来的心跳，follower是否宕机
        // follower来说，他需要定时检查leader发送过来的心跳，leader是否宕机
        if (followerAliveCheckScheduler != null) {
            followerAliveCheckScheduler.cancel();
        }
        if (leaderToFollowerHeartbeatScheduler != null) {
            leaderToFollowerHeartbeatScheduler.cancel();
        }
        if (followerHeartbeatTaskScheduler != null) {
            followerHeartbeatTaskScheduler.cancel();
        }
        if (leaderAliveCheckScheduler != null) {
            leaderAliveCheckScheduler.cancel();
        }
    }

}
