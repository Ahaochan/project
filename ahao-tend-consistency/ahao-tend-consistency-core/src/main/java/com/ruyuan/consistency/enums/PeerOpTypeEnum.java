package com.ruyuan.consistency.enums;

/**
 * 集群节点操作的枚举
 *
 * @author zhonghuashishan
 **/
public enum PeerOpTypeEnum {

    /**
     * 节点上线
     */
    ONLINE(1),
    /**
     * 节点下线
     */
    OFFLINE(2),
    ;

    private Integer opType;

    PeerOpTypeEnum(Integer opType) {
        this.opType = opType;
    }

    public Integer getOpType() {
        return opType;
    }

}
