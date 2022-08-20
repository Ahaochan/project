package moe.ahao.tend.consistency.core.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 集群节点操作的枚举
 **/
@Getter
@AllArgsConstructor
public enum PeerOpTypeEnum {
    ONLINE(1, "节点上线"),
    OFFLINE(2, "节点下线"),
    ;
    private final Integer code;
    private final String name;
}
