package moe.ahao.spring.boot.statemachine.squirrel.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单操作类型枚举值
 */
@Getter
@AllArgsConstructor
public enum OrderOperateTypeEnum {
    NEW_ORDER(10, "新建订单"),
    PRE_PAY_ORDER(11, "订单预支付"),
    PAID_ORDER(40, "完成订单支付"),
    ;
    private final Integer code;
    private final String name;
}
