package moe.ahao.tend.consistency.core.adapter.message;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * leader向follower发送的心跳请求
 **/
@Data
public class LeaderToFollowerHeartbeatRequest {
    /**
     * 当前leader id
     */
    private Integer currentLeaderId;
    /**
     * leader节点的分片结果
     */
    private Map<String, List<Long>> cacheSharingResult;
    /**
     * 分片结果的md5值 用于follower节点进行校验 是否需要更新本地的分片结果
     */
    private String checksum;
}
