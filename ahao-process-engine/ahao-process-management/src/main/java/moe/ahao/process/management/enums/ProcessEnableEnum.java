package moe.ahao.process.management.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 流程启用/禁用类型
 */
@Getter
@AllArgsConstructor
public enum ProcessEnableEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;
    private final String msg;

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (ProcessEnableEnum typeEnum : values()) {
            allowableValues.add(typeEnum.getCode());
        }
        return allowableValues;
    }
}
