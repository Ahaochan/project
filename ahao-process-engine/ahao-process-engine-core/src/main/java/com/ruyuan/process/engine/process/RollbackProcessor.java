package com.ruyuan.process.engine.process;

/**
 * 可回滚的流程
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public abstract class RollbackProcessor extends AbstractProcessor {

    /**
     * 回滚操作
     *
     * @param context 上下文
     */
    protected abstract void rollback(ProcessContext context);

}
