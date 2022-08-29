package moe.ahao.process.engine.core.node;

import moe.ahao.process.engine.core.ProcessContext;

/**
 * 可回滚的流程动作
 */
public abstract class RollbackProcessorNode extends AbstractProcessorNode {
    /**
     * 回滚操作
     *
     * @param context 上下文
     */
    public abstract void rollback(ProcessContext context);
}
