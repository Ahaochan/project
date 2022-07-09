package moe.ahao.spring.boot.statemachine.squirrel.action;

public abstract class AbstractStateAction<C, R, E> implements StateAction<E> {

    @Override
    @SuppressWarnings("unchecked")
    public void onStateChange(E event, Object context) {
        R r = this.onStateChangeInternal(event, (C) context);
        this.postStateChange(event, r);
    }

    /**
     * 状态变更后置操作
     *
     * @param event   事件
     * @param context 上下文
     */
    protected void postStateChange(E event, R context) {
        // 默认空实现，提供给子类一个钩子
    }

    /**
     * 状态变更操作
     *
     * @param event   事件
     * @param context 上下文
     * @return 返回标准的数据
     */
    protected abstract R onStateChangeInternal(E event, C context);

}
