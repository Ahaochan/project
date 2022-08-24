package com.ruyuan.process.engine.process;

/**
 * 抽象流程，提供了流程包装功能
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public abstract class AbstractProcessor implements Processor {

    private String name;

    @Override
    public void process(ProcessContext context) {
        beforeProcess(context);
        processInternal(context);
        afterProcess(context);
    }

    /**
     * 流程核心逻辑
     *
     * @param context 上下文
     */
    protected abstract void processInternal(ProcessContext context);

    /**
     * 流程前操作
     *
     * @param context 上下文
     */
    protected void beforeProcess(ProcessContext context) {
        // default no-op
    }

    /**
     * 流程后的操作
     *
     * @param context 上下文
     */
    protected void afterProcess(ProcessContext context) {
        // default op-op
    }

    @Override
    public void caughtException(ProcessContext context, Throwable throwable) {
        // default no-op
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
