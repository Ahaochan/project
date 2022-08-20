package com.ruyuan.consistency.enums;

/**
 * 通信URL模板的枚举
 *
 * @author zhonghuashishan
 **/
public enum PeerTransportEnum {

    /**
     * leader向follower发送心跳的URL模板
     */
    LEADER_HEARTBEAT_URL_TEMPLATE("http://%s/follower/heartbeat"),
    /**
     * leader注册一个节点的URL模板
     */
    REGISTRY_URL_TEMPLATE("http://%s/common/registerOrCancel"),
    /**
     * FOLLOWER向LEADER发送心跳的URL模板
     */
    FOLLOWER_HEARTBEAT_URL_TEMPLATE("http://%s/leader/heartbeat"),
    ;

    private String url;

    PeerTransportEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
