package moe.ahao.tend.consistency.core.sharding;

import lombok.Data;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatRequest;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 一致性任务分片上下文
 **/
@Data
public class ConsistencyTaskShardingContext {
    private static volatile ConsistencyTaskShardingContext instance;
    public static ConsistencyTaskShardingContext getInstance() {
        if (instance == null) {
            synchronized (ConsistencyTaskShardingContext.class) {
                if (instance == null) {
                    instance = new ConsistencyTaskShardingContext();
                }
            }
        }
        return instance;
    }

    /**
     * 当前的leader节点id
     */
    private PeerNodeId currentLeaderPeerId;
    /**
     * 当前节点的id
     */
    private PeerNodeId currentPeerId;
    /**
     * 分片结果 key peerId value 分片好的shardIndex集合
     */
    private Map<PeerNodeId, List<Long>> taskSharingResult;
    /**
     * 分片结果校验和 是一个md5值 用于follower收到leader心跳请求时，对分片结果进行校验
     */
    private String checksum;

    /**
     * 根据leaderToFollowerHeartbeatRequest请求对象 设置分片上下文 follower收到leader的分片结果时，使用
     *
     * @param leaderToFollowerHeartbeatRequest leader往follower发送心跳请求
     */
    public void updateTaskSharingResult(LeaderToFollowerHeartbeatRequest leaderToFollowerHeartbeatRequest) {
        // 如果校验和为空或校验和与本地的不同, 就更新分片集合
        if (StringUtils.isNotEmpty(checksum) && Objects.equals(leaderToFollowerHeartbeatRequest.getChecksum(), checksum)) {
            return;
        }
        this.checksum =  leaderToFollowerHeartbeatRequest.getChecksum();
        this.taskSharingResult  = leaderToFollowerHeartbeatRequest.getCacheSharingResult().entrySet().stream()
            .collect(Collectors.toMap(s -> new PeerNode(s.getKey()).getId(), Map.Entry::getValue));
    }
}
