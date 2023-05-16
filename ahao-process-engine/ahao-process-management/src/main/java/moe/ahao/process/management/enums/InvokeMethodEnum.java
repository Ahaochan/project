package moe.ahao.process.management.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用方式
 */
@Getter
@AllArgsConstructor
public enum InvokeMethodEnum {
    SYNC("SYNC", "同步"),
    ASYNC("ASYNC", "异步");

    private final String code;
    private final String desc;

    public static InvokeMethodEnum getByCode(String code) {
        for (InvokeMethodEnum element : InvokeMethodEnum.values()) {
            if (element.getCode().equals(code)) {
                return element;
            }
        }
        return null;
    }
}
