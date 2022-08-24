package com.ruyuan.process.engine.demo;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.RollbackProcessor;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class RollBackProcessorDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollBackProcessor " + context.get("id"));
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollBackProcessor " + context.get("id"));
    }
}