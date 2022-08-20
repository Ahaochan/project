package moe.ahao.tend.consistency.core.adapter.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * leader向follower发送的心跳响应
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderToFollowerHeartbeatResponse {

    /**
     * 心跳重置是否成功
     */
    private boolean success;
    /**
     * 当前节点id  即回复心跳响应的节点id
     */
    private Integer responsePeerId;
    /**
     * 最近一次回复给leader节点的时间戳
     */
    private Long lastResponseTs;

}
