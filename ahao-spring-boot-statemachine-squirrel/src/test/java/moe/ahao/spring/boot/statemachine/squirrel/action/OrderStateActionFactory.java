package moe.ahao.spring.boot.statemachine.squirrel.action;

import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusChangeEnum;

import java.util.List;

public class OrderStateActionFactory {
    private final List<OrderStateAction<?>> actions;
    public OrderStateActionFactory(List<OrderStateAction<?>> actions) {
        this.actions = actions;
    }

    public OrderStateAction<?> getAction(OrderStatusChangeEnum event) {
        for (OrderStateAction<?> action : actions) {
            if (action.event() == null) {
                throw new IllegalArgumentException("event 返回值不能为空：" + action.getClass().getSimpleName());
            }
            if (action.event().equals(event)) {
                return action;
            }
        }
        return null;
    }
}
