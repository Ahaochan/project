package moe.ahao.process.management.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 流程类型
 */
@Getter
@AllArgsConstructor
public enum ProcessTypeEnum {
    ORDER(1, "正向"),
    AFTER_SALE(2, "逆向");
    private final Integer code;
    private final String msg;

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (ProcessTypeEnum typeEnum : values()) {
            allowableValues.add(typeEnum.getCode());
        }
        return allowableValues;
    }
}
