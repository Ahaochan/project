package moe.ahao.tend.consistency.core.adapter.scheduler.follower;

import moe.ahao.tend.consistency.core.adapter.message.ReShardTaskAndReElectionEvent;
import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * follower用于检测leader是否存活的调度器
 **/
@Component
public class LeaderAliveCheckScheduler extends AbstractSingleScheduler {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    @Autowired
    private PeerNodeManager peerNodeManager;

    public LeaderAliveCheckScheduler() {
        super("leaderAliveScheduler");
    }

    @Override
    protected Runnable task() {
        return this::doLeaderAliveCheck;
    }

    /**
     * follower检测leader是否存活
     */
    private void doLeaderAliveCheck() {
        // 每隔10s跑一次检查，如果说上一次leader心跳响应，到现在为止，超过了120s了
        // 此时就可以认为说你的leader也是宕机了
        // 如果follower超过指定阈值还无法与leader通信，判定为leader宕机重新选举，重新下发任务分片
        if (System.currentTimeMillis() - peerNodeManager.getRecentlyFollowerToLeaderHeartbeatResponse().getReplyTimestamp()
            > tendConsistencyConfiguration.getJudgeLeaderDownSecondsThreshold() * 1000L) {
            PeerNode leaderPeerNode = peerNodeManager.getLeaderPeerNode();
            peerNodeManager.getPeerNodes().remove(leaderPeerNode);
            // 重新选举和任务分片
            applicationEventPublisher.publishEvent(new ReShardTaskAndReElectionEvent());
        }
    }

    @Override
    protected int delaySecond() {
        return tendConsistencyConfiguration.getLeaderAliveCheckIntervalSeconds();
    }
}
