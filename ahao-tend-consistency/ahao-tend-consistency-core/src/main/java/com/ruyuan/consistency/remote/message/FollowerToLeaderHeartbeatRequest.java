package com.ruyuan.consistency.remote.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * follower发送给leader的心跳请求
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerToLeaderHeartbeatRequest {

    /**
     * 节点id
     */
    private String peerId;

}
