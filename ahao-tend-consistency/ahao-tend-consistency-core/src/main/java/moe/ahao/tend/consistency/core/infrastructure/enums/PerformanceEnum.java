package moe.ahao.tend.consistency.core.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行模式枚举
 **/
@Getter
@AllArgsConstructor
public enum PerformanceEnum {
    NOW(1, "立即执行"),
    SCHEDULE(2, "调度执行"),
    ;
    private final Integer code;
    private final String name;
}
