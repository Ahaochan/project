package moe.ahao.spring.boot.statemachine.squirrel.action;

import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusChangeEnum;

public abstract class OrderStateAction<C> extends AbstractStateAction<C, String, OrderStatusChangeEnum> implements StateAction<OrderStatusChangeEnum> {
    @Override
    protected void postStateChange(OrderStatusChangeEnum event, String context) {
        if (context == null) {
            return;
        }
        if (event.isSendEvent()) {
            System.out.println(this.getClass() + "发送订单状态变更消息, context:" + context + ", event:" + event);
        }
    }
}
