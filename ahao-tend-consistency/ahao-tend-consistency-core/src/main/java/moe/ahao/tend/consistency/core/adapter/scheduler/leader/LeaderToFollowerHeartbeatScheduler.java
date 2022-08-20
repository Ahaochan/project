package moe.ahao.tend.consistency.core.adapter.scheduler.leader;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatRequest;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.infrastructure.gateway.PeerNodeGateway;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * leader定时发给follower的心跳的调度器，同时也会将leader对任务的分片信息发送给各个follower节点
 **/
@Slf4j
public class LeaderToFollowerHeartbeatScheduler extends AbstractSingleScheduler {
    private final PeerNodeGateway peerNodeGateway;
    private final PeerNodeManager peerNodeManager;
    private final ConsistencyTaskShardingContext consistencyTaskShardingContext;

    public LeaderToFollowerHeartbeatScheduler(PeerNodeGateway peerNodeGateway, int initDelaySecond, int delaySecond) {
        super("leaderHeartScheduler", initDelaySecond, delaySecond);
        this.peerNodeGateway = peerNodeGateway;
        this.peerNodeManager = PeerNodeManager.getInstance();
        this.consistencyTaskShardingContext = ConsistencyTaskShardingContext.getInstance();
    }

    @Override
    protected Runnable task() {
        return this::sendHeartbeatTask;
    }

    /**
     * leader发送心跳包给其他节点，同时也会将分片信息发给其他节点
     *
     * @see moe.ahao.tend.consistency.core.adapter.http.FollowerController#leaderHeartbeat(LeaderToFollowerHeartbeatRequest)
     */
    private void sendHeartbeatTask() {
        log.info("进入发送心跳的定时调度任务");

        // 1. 从分片上下文获取Leader节点, 也就是自己
        PeerNodeId currentLeaderId = consistencyTaskShardingContext.getCurrentLeaderPeerId();

        // 2. 构造leader发送心跳给follower的请求体
        LeaderToFollowerHeartbeatRequest request = this.buildRequest();

        // 3. 遍历所有follower节点, 将最新的分片结果发送给各个follower
        Map<PeerNodeId, PeerNode> peerInfoMap = peerNodeManager.getAvailableShardingInstances();
        for (Map.Entry<PeerNodeId, PeerNode> entry : peerInfoMap.entrySet()) {
            PeerNodeId key = entry.getKey();
            PeerNode peerNode = entry.getValue();
            if (key.equals(currentLeaderId)) {
                // 3.1. 跳过自己
                continue;
            }
            // 3.2. 通过http请求发送心跳给follower, 然后更新心跳表
            LeaderToFollowerHeartbeatResponse response = peerNodeGateway.doSendHeartbeatTask(peerNode, request);
            if (response != null) {
                peerNodeManager.getHeartbeatResponseTable().put(new PeerNodeId(response.getResponsePeerId()), response);
            }
        }
    }

    private LeaderToFollowerHeartbeatRequest buildRequest() {
        PeerNodeId currentLeaderId = consistencyTaskShardingContext.getCurrentLeaderPeerId();
        Map<String, List<Long>> cacheShardingResult = consistencyTaskShardingContext.getTaskSharingResult().entrySet().stream()
            .collect(Collectors.toMap(s -> s.getKey().toString(), Map.Entry::getValue));
        LeaderToFollowerHeartbeatRequest request = new LeaderToFollowerHeartbeatRequest();
        request.setCurrentLeaderId(currentLeaderId.getVal());
        request.setCacheSharingResult(cacheShardingResult);
        request.setChecksum(consistencyTaskShardingContext.getChecksum());
        return request;
    }
}
