package moe.ahao.process.engine.core.process;

/**
 * 流程引擎
 */
public interface Processor {

    /**
     * 执行逻辑
     *
     * @param context 上下文
     */
    void process(ProcessContext context);

    /**
     * 触发异常了
     *
     * @param context   上下文
     * @param throwable 异常
     */
    void caughtException(ProcessContext context, Throwable throwable);

    /**
     * 设置名字
     *
     * @param name 名字
     */
    void setName(String name);

    /**
     * 获取名字
     *
     * @return 获取名字
     */
    String getName();
}
