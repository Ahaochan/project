package moe.ahao.process.engine.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程之间的调用方式
 */
@Getter
@AllArgsConstructor
public enum InvokeMethod {
    SYNC("同步"),
    ASYNC("异步"),
    ;
    private final String name;
}
