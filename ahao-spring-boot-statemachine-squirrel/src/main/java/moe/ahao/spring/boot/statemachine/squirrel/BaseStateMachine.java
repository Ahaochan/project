package moe.ahao.spring.boot.statemachine.squirrel;

import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

/**
 * 状态机父类
 */
public abstract class BaseStateMachine<S, E> extends AbstractUntypedStateMachine {
    public static final String METHOD_NAME = "onStateChange";
    private final ThreadLocal<Exception> exceptionThreadLocal = new ThreadLocal<>();

    /**
     * 状态流传
     *
     * @param fromStatus from状态
     * @param toState    to状态
     * @param event      通过event事件, 将from状态扭转为to状态
     * @param context    上下文
     */
    public void onStateChange(S fromStatus, S toState, E event, Object context) {
        try {
            // 发生了什么事件，event，from，to，传递进来的订单创建request对象会作为context传递进来
            this.onStateChangeInternal(fromStatus, toState, event, context);
        } catch (Exception e) {
            exceptionThreadLocal.set(e);
        }
    }

    /**
     * 正常情况下状态机：调用fire(event,context) 方法，会调用onStateChange方法。
     * <p>
     * 假如onStateChange方法抛出业务异常，这里会被状态机接管，然后使用一个Squirrel-Foundation内部的异常
     * TransitionException对我们的业务异常进行包装。然后抛出TransitionException异常。
     * <p>
     * 我们一般的情景：在SpringBoot中调用状态机开始状态流转，调用了fire方法，接着得到一个TransitionException异常，
     * 显然不是我们想要的结果。我们希望onStateChange方法抛出的如果是业务异常BaseBizException，则fire方法抛出的也是业务异常。
     * <p>
     * 所以这里采用了一种方式，在onStateChange方法中使用ThreadLocal将状态保存起来，
     * 那么fire方法就无法检测到我们实际业务代码是否抛出了异常，此时等fire方法返回的时候，我们再判断ThreadLocal中是否有异常，
     * 如果有就直接抛出，这样就可以实现我们所需要的结果。
     */
    @Override
    public void fire(Object event, Object context) {
        super.fire(event, context); // 调用父类的fire，很明显干什么，触发一个事件
        // 就会从这个事件指定的null -> created，触发onStateChange方法
        Exception exception = exceptionThreadLocal.get();
        if (exception != null) {
            exceptionThreadLocal.remove();
            // if (exception instanceof BaseBizException) {
            //     throw (BaseBizException) exception;
            // } else {
                throw new RuntimeException(exception);
            // }
        }
    }

    /**
     * 状态机装填流转核心逻辑
     */
    protected abstract void onStateChangeInternal(S fromStatus, S toState, E event, Object context);
}
