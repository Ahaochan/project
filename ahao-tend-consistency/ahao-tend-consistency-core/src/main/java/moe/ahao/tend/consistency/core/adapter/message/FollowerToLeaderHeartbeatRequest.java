package moe.ahao.tend.consistency.core.adapter.message;

import lombok.Data;

/**
 * follower发送给leader的心跳请求
 **/
@Data
public class FollowerToLeaderHeartbeatRequest {
    /*
     * 节点id
     */
    private Integer peerId;

}
