package moe.ahao.spring.boot.statemachine.squirrel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单号状态枚举
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    NULL(0, "未知"),
    CREATED(10, "已创建"),
    PAID(20, "已支付")
    ;
    private final Integer code;
    private final String name;
}
