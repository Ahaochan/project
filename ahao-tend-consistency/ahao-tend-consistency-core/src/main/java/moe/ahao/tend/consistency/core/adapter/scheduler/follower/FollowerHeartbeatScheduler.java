package moe.ahao.tend.consistency.core.adapter.scheduler.follower;

import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatRequest;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.infrastructure.gateway.PeerNodeGateway;
import moe.ahao.tend.consistency.core.spi.shard.shardingstrategy.ConsistencyTaskShardingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * follower对leader发送心跳使用的调度器
 **/
@Component
public class FollowerHeartbeatScheduler extends AbstractSingleScheduler {
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    @Autowired
    private PeerNodeGateway peerNodeGateway;
    @Autowired
    private PeerNodeManager peerNodeManager;

    private final ConsistencyTaskShardingContext consistencyTaskShardingContext;

    public FollowerHeartbeatScheduler() {
        super("followerHeartbeat");
        this.consistencyTaskShardingContext = ConsistencyTaskShardingContext.getInstance();
    }

    @Override
    protected Runnable task() {
        return this::sendFollowerHeartbeatRequest;
    }

    /**
     * follower向leader发送心跳请求
     */
    private void sendFollowerHeartbeatRequest() {
        // 获取leader节点的 ip:port
        PeerNode peerNode = peerNodeManager.getAvailableShardingInstances().get(peerNodeManager.getSelfPeerNode().getId());
        // 构造发送给leader的心跳请求对象
        FollowerToLeaderHeartbeatRequest request = new FollowerToLeaderHeartbeatRequest();
        request.setPeerId(peerNodeManager.getSelfPeerNode().getId().getVal());
        FollowerToLeaderHeartbeatResponse response = peerNodeGateway.sendFollowerHeartbeatRequest(peerNode, request);
        if (response != null) {
            peerNodeManager.setRecentlyFollowerToLeaderHeartbeatResponse(response);
        }
    }

    @Override
    protected int delaySecond() {
        return tendConsistencyConfiguration.getFollowerHeartbeatIntervalSeconds();
    }
}
