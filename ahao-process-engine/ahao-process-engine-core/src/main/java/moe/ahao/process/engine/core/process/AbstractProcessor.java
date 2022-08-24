package moe.ahao.process.engine.core.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象流程，提供了流程包装功能
 */
@Slf4j
@NoArgsConstructor
public abstract class AbstractProcessor implements Processor {
    @Setter
    @Getter
    private String name;

    public AbstractProcessor(String name) {
        this.name = name;
    }

    @Override
    public void process(ProcessContext context) {
        this.beforeProcess(context);
        this.processInternal(context);
        this.afterProcess(context);
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
        log.error("流程节点动作" + name + "执行错误", throwable);
    }
}
