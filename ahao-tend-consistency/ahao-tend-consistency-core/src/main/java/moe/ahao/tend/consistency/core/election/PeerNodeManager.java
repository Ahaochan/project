package moe.ahao.tend.consistency.core.election;


import lombok.Getter;
import lombok.Setter;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatResponse;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.utils.NetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PeerNodeManager {
    @Value("${server.port}")
    private int currentServerPort;

    /**
     * follower回复给leader的心跳响应表 格式： key: PeerId value: HeartbeatResponse
     * leader对应的每个follower，每次收到一个leader心跳，返回了一个响应，leader来说，他拿到的每个响应，都会根据follower peer id
     * key-value写入，覆盖
     * 对于leader来说，就可以检查到每个follower最近一次接收到心跳返回的一个响应
     */
    @Getter
    private final Map<PeerNodeId, LeaderToFollowerHeartbeatResponse> heartbeatResponseTable = new HashMap<>();
    /**
     * follower发送给leader的心跳响应
     */
    @Setter
    @Getter
    private FollowerToLeaderHeartbeatResponse recentlyFollowerToLeaderHeartbeatResponse;
    /**
     * 一致性框架集群节点的配置信息
     */
    @Getter
    @Setter
    private List<PeerNode> peerNodes;
    /**
     * 可用于执行分片任务的实例信息 格式：key:peerId, value:ip:port
     */
    @Getter
    private final Map<PeerNodeId, PeerNode> availableShardingInstances = new HashMap<>();
    /**
     * 当前的leader节点id
     */
    @Getter
    private PeerNode leaderPeerNode;
    /**
     * 当前节点
     */
    @Getter
    private PeerNode selfPeerNode;

    public void initPeerNodes(List<PeerNode> peerNodes) {
        this.peerNodes = peerNodes;
        String currentPeerAddress = NetUtils.getCurrentPeerAddress() + ":" + currentServerPort;

        for (PeerNode peerNode : peerNodes) {
            // 构造可用于分片集群节点的实例信息map, key:peerId, value:ip:port
            availableShardingInstances.put(peerNode.getId(), peerNode);

            // 获取当前节点的peerId
            if (peerNode.getAddress().equals(currentPeerAddress)) {
                this.selfPeerNode = peerNode;
            }
        }
    }

    public PeerNode updateLeaderId(PeerNodeId peerNodeId) {
        this.leaderPeerNode = this.getAvailableShardingInstances().get(peerNodeId);
        return this.leaderPeerNode;
    }

    /**
     * 选举一个作为leader, 现在是取最小节点id作为leader
     * @return 最小节点id
     */
    public PeerNode electionLeaderNode() {
        return peerNodes.stream()
            .min(Comparator.comparing(o -> o.getId().getVal()))
            .orElse(null);
    }


    public boolean selfIsLeader() {
        return Objects.equals(selfPeerNode, leaderPeerNode);
    }
}
