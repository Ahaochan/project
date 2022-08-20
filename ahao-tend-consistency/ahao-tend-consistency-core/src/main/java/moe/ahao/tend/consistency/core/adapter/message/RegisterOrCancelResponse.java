package moe.ahao.tend.consistency.core.adapter.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上线或下线响应
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterOrCancelResponse {

    /**
     * 应答节点的id
     */
    private Integer replyPeerId;
    /**
     * 接收到请求的节点是否是leader节点
     */
    private boolean leader;
    /**
     * 请求对象
     */
    private RegisterOrCancelRequest registerOrCancelRequest;

    public RegisterOrCancelResponse(boolean isLeader) {
        this.leader = isLeader;
    }

}
