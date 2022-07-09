package moe.ahao.spring.boot.statemachine.squirrel;

import moe.ahao.spring.boot.statemachine.squirrel.action.OrderCreateAction;
import moe.ahao.spring.boot.statemachine.squirrel.action.OrderPaidAction;
import moe.ahao.spring.boot.statemachine.squirrel.action.OrderPrepareAction;
import moe.ahao.spring.boot.statemachine.squirrel.action.OrderStateActionFactory;
import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusChangeEnum;
import moe.ahao.spring.boot.statemachine.squirrel.enums.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;

import java.util.Arrays;

public class SquirrelTest {
    @Test
    public void order() {
        // 构建一个订单状态机的builder工厂
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(OrderStateMachine.class);

        // 定义状态机的扭转动作
        for (OrderStatusChangeEnum event : OrderStatusChangeEnum.values()) {
            builder.externalTransition()
                .from(event.getFromStatus())
                .to(event.getToStatus())
                .on(event)
                .callMethod(BaseStateMachine.METHOD_NAME);
        }
        OrderStateActionFactory orderStateActionFactory = new OrderStateActionFactory(Arrays.asList(
            new OrderCreateAction(), new OrderPaidAction(), new OrderPrepareAction()
        ));

        // 我们还把创建订单request对象传递进来了，这个对象，是需要后面来进行使用的
        // 状态机，初始的状态是null，创建事件，触发了以后，会从null -> created，流转之后，会在这里触发状态机里的方法的执行
        this.getOrderStateMachine(builder, orderStateActionFactory, OrderStatusEnum.NULL).fire(OrderStatusChangeEnum.ORDER_CREATED, "订单一号");
        this.getOrderStateMachine(builder, orderStateActionFactory, OrderStatusEnum.CREATED).fire(OrderStatusChangeEnum.ORDER_PREPAY, "订单一号");
    }

    private OrderStateMachine getOrderStateMachine(UntypedStateMachineBuilder builder, OrderStateActionFactory actionFactory, OrderStatusEnum initState) {
        OrderStateMachine orderStateMachine = builder.newUntypedStateMachine(initState);
        orderStateMachine.setOrderStateActionFactory(actionFactory);
        return orderStateMachine;
    }
}
