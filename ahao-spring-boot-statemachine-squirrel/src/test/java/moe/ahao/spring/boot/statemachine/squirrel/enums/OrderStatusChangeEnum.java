package moe.ahao.spring.boot.statemachine.squirrel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态变化枚举
 */
@Getter
@AllArgsConstructor
public enum OrderStatusChangeEnum {
    // 订单已创建
    ORDER_CREATED(OrderStatusEnum.NULL, OrderStatusEnum.CREATED, OrderOperateTypeEnum.NEW_ORDER, "created"),
    // 订单预支付
    ORDER_PREPAY(OrderStatusEnum.CREATED, OrderStatusEnum.CREATED, OrderOperateTypeEnum.PRE_PAY_ORDER, false),
    // 订单已支付
    ORDER_PAID(OrderStatusEnum.CREATED, OrderStatusEnum.PAID, OrderOperateTypeEnum.PAID_ORDER, "paid")
    ;
    OrderStatusChangeEnum(OrderStatusEnum fromStatus, OrderStatusEnum toStatus, OrderOperateTypeEnum operateType, String tags) {
        this(fromStatus, toStatus, operateType, tags, true);
    }

    OrderStatusChangeEnum(OrderStatusEnum fromStatus, OrderStatusEnum toStatus, OrderOperateTypeEnum operateType, boolean sendEvent) {
        this(fromStatus, toStatus, operateType, null, sendEvent);
    }

    private final OrderStatusEnum fromStatus;
    private final OrderStatusEnum toStatus;
    private final OrderOperateTypeEnum operateType;
    private final String tags;
    private final boolean sendEvent;
}
