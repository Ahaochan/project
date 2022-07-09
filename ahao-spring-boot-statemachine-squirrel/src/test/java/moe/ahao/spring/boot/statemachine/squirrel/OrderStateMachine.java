package moe.ahao.spring.boot.statemachine.squirrel;

import lombok.Setter;
import moe.ahao.spring.boot.statemachine.squirrel.action.OrderStateAction;
import moe.ahao.spring.boot.statemachine.squirrel.action.OrderStateActionFactory;
import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusChangeEnum;
import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusEnum;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;

@StateMachineParameters(stateType = OrderStatusEnum.class, eventType = OrderStatusChangeEnum.class, contextType = Object.class)
public class OrderStateMachine extends BaseStateMachine<OrderStatusEnum, OrderStatusChangeEnum> {
    @Setter
    private OrderStateActionFactory orderStateActionFactory;

    @Override
    public void onStateChange(OrderStatusEnum fromStatus, OrderStatusEnum toState, OrderStatusChangeEnum event, Object context) {
        super.onStateChange(fromStatus, toState, event, context);
    }

    @Override
    public void onStateChangeInternal(OrderStatusEnum fromStatus, OrderStatusEnum toState, OrderStatusChangeEnum event, Object context) {
        OrderStateAction<?> action = orderStateActionFactory.getAction(event);
        if (action != null) {
            action.onStateChange(event, context);
        }
    }
}
