package moe.ahao.tend.consistency.core.election;


import lombok.Getter;
import lombok.Setter;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatResponse;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeerNodeManager {
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

    private static volatile PeerNodeManager instance;
    public static PeerNodeManager getInstance() {
        if (instance == null) {
            synchronized (PeerNodeManager.class) {
                if (instance == null) {
                    instance = new PeerNodeManager();
                }
            }
        }
        return instance;
    }

    public void initPeerNodes(List<PeerNode> peerNodes) {
        this.peerNodes = peerNodes;
        for (PeerNode peerNode : peerNodes) {
            // 构造可用于分片集群节点的实例信息map, key:peerId, value:ip:port
            availableShardingInstances.put(peerNode.getId(), peerNode);
        }
    }
}
