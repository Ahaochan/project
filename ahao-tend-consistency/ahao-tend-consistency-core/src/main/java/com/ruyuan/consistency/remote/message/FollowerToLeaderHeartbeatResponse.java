package com.ruyuan.consistency.remote.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * follower发送给leader的心跳响应
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerToLeaderHeartbeatResponse {

    /**
     * 是否成功拿到响应消息
     */
    private boolean success;
    /**
     * 回复时间  用来判断 是否leader宕机了
     */
    private Long replyTimestamp;

}
