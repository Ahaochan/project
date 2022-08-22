package moe.ahao.tend.consistency.core.adapter.message;

import lombok.Data;

/**
 * 上线或下线请求
 **/
@Data
public class RegisterOrCancelRequest {
    /**
     * follower的ip地址
     */
    private String ip;
    /**
     * follower的端口号
     */
    private String port;
    /**
     * follower的peerId
     */
    private Integer peerId;
    /**
     * 操作类型 1:上线 2:下线
     */
    private Integer opType;
    /**
     * 是否是leader节点下线
     */
    private boolean leaderOffline;
}
