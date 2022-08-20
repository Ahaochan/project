package com.ruyuan.consistency.enums;

/**
 * 执行模式枚举
 *
 * @author zhonghuashishan
 **/
public enum PerformanceEnum {

    /**
     * 立即执行
     */
    PERFORMANCE_RIGHT_NOW(1, "立即执行"),
    /**
     * 调度执行
     */
    PERFORMANCE_SCHEDULE(2, "调度执行");


    private final Integer code;

    private final String desc;

    PerformanceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
