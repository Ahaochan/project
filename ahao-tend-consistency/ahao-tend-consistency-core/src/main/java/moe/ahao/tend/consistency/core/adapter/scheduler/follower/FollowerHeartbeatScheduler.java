package moe.ahao.tend.consistency.core.adapter.scheduler.follower;

import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatRequest;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.infrastructure.gateway.PeerNodeGateway;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;

/**
 * follower对leader发送心跳使用的调度器
 **/
public class FollowerHeartbeatScheduler extends AbstractSingleScheduler {
    private final PeerNodeGateway peerNodeGateway;
    private final PeerNodeManager peerNodeManager;
    private final ConsistencyTaskShardingContext consistencyTaskShardingContext;

    public FollowerHeartbeatScheduler(PeerNodeGateway peerNodeGateway,int initDelaySecond, int delaySecond) {
        super("followerHeartbeat", initDelaySecond, delaySecond);
        this.peerNodeGateway = peerNodeGateway;
        this.peerNodeManager = PeerNodeManager.getInstance();
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
        PeerNode peerNode = PeerNodeManager.getInstance().getAvailableShardingInstances().get(consistencyTaskShardingContext.getCurrentLeaderPeerId());
        // 构造发送给leader的心跳请求对象
        FollowerToLeaderHeartbeatRequest request = new FollowerToLeaderHeartbeatRequest();
        request.setPeerId(consistencyTaskShardingContext.getCurrentPeerId().getVal());
        FollowerToLeaderHeartbeatResponse response = peerNodeGateway.sendFollowerHeartbeatRequest(peerNode, request);
        if (response != null) {
            peerNodeManager.setRecentlyFollowerToLeaderHeartbeatResponse(response);
        }
    }
}
