package moe.ahao.process.engine.core.node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.ProcessContext;

import java.util.UUID;

/**
 * 抽象流程节点处理器，提供了流程包装功能
 */
@Slf4j
public abstract class AbstractProcessorNode implements ProcessorNode {
    @Getter
    private String name;
    public AbstractProcessorNode() {
        this(null);
        this.setName(this.getClass().getSimpleName() + UUID.randomUUID());
    }
    public AbstractProcessorNode(String name) {
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

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
