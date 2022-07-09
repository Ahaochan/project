package moe.ahao.spring.boot.statemachine.squirrel.action;

/**
 * 状态变更Action
 */
public interface StateAction<E> {
    /**
     * 当前处理器处理哪种事件
     *
     * @return 事件
     */
    E event();

    /**
     * 状态变更
     *
     * @param event   状态变更事件
     * @param context 上下文信息
     */
    void onStateChange(E event, Object context);
}
