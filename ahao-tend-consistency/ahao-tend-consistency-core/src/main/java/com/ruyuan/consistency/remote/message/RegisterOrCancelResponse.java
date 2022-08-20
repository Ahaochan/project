package com.ruyuan.consistency.remote.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上线或下线响应
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterOrCancelResponse {

    /**
     * 应答节点的id
     */
    private String replyPeerId;
    /**
     * 接收到请求的节点是否是leader节点
     */
    private boolean leader;
    /**
     * 请求对象
     */
    private RegisterOrCancelRequest registerOrCancelRequest;

}
