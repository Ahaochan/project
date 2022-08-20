package moe.ahao.tend.consistency.core.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一致性任务状态枚举类
 **/
@Getter
@AllArgsConstructor
public enum ConsistencyTaskStatusEnum {
    INIT(0, "初始化"),
    START(1, "开始执行"),
    FAIL(2, "执行失败"),
    SUCCESS(3, "执行成功"),
    ;
    private final Integer code;
    private final String name;
}
