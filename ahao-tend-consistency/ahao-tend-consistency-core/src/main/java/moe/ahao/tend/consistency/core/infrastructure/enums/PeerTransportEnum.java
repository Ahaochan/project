package moe.ahao.tend.consistency.core.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通信URL模板的枚举
 **/
@Getter
@AllArgsConstructor
public enum PeerTransportEnum {
    LEADER_HEARTBEAT_URL_TEMPLATE("http://%s/follower/heartbeat", "leader向follower发送心跳的URL模板"),
    REGISTRY_URL_TEMPLATE("http://%s/common/registerOrCancel", "leader注册一个节点的URL模板"),
    FOLLOWER_HEARTBEAT_URL_TEMPLATE("http://%s/leader/heartbeat", "FOLLOWER向LEADER发送心跳的URL模板"),
    ;
    private final String url;
    private final String name;
}
