package moe.ahao.process.engine.core.process;

import lombok.NoArgsConstructor;

/**
 * 可回滚的流程动作
 */
@NoArgsConstructor
public abstract class RollbackProcessor extends AbstractProcessor {
    public RollbackProcessor(String name) {
        super(name);
    }

    /**
     * 回滚操作
     *
     * @param context 上下文
     */
    protected abstract void rollback(ProcessContext context);
}
