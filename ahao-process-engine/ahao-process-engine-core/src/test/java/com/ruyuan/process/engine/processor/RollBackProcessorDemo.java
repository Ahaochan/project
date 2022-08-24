package com.ruyuan.process.engine.processor;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.RollbackProcessor;
import com.ruyuan.process.engine.process.StandardProcessor;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class RollBackProcessorDemo extends RollbackProcessor {

    private String id;
    private boolean throwException = false;

    public RollBackProcessorDemo(String id, boolean throwException) {
        this.throwException = throwException;
        this.id = id;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollBackProcessor " + id);
        if (throwException) {
            int i = 1 / 0;
        }
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollBackProcessor " + id);
    }
}
