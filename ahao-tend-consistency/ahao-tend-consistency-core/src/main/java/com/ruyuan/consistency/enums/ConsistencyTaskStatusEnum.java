package com.ruyuan.consistency.enums;

/**
 * 一致性任务状态枚举类
 *
 * @author zhonghuashishan
 **/
public enum ConsistencyTaskStatusEnum {

    /**
     * 0:初始化 1:开始执行 2:执行失败 3:执行成功
     */
    INIT(0),
    START(1),
    FAIL(2),
    SUCCESS(3);

    private final Integer code;

    ConsistencyTaskStatusEnum(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
