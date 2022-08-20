package com.ruyuan.consistency.sharding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 一致性任务分片上下文
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsistencyTaskShardingContext {

    /**
     * 当前的leader节点id
     */
    private String currentLeaderPeerId;
    /**
     * 当前节点的id
     */
    private String currentPeerId;
    /**
     * 分片结果 key peerId value 分片好的shardIndex集合
     */
    private Map<String, List<Long>> taskSharingResult;
    /**
     * 分片结果校验和 是一个md5值 用于follower收到leader心跳请求时，对分片结果进行校验
     */
    private String checksum;

}
