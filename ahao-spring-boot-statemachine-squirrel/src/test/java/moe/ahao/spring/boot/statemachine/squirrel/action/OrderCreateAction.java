package moe.ahao.spring.boot.statemachine.squirrel.action;

import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusChangeEnum;

public class OrderCreateAction extends OrderStateAction<String> {
    @Override
    public OrderStatusChangeEnum event() {
        return OrderStatusChangeEnum.ORDER_CREATED;
    }

    @Override
    protected String onStateChangeInternal(OrderStatusChangeEnum event, String context) {
        System.out.println(this.getClass() + "订单状态变更, context:" + context + ", event:" + event);
        return context + "1";
    }
}
