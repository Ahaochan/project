package moe.ahao.tend.consistency.core.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 线程模型枚举
 **/
@Getter
@AllArgsConstructor
public enum ThreadWayEnum {
    ASYNC(1, "异步执行"),
    SYNC(2, "同步执行"),
    ;
    private final Integer code;
    private final String desc;
}
