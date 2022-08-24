package com.ruyuan.process.engine.demo;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.RollbackProcessor;

/**
 * 6
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class RollbackProcessorThrowExceptionDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println(Thread.currentThread().getName() + " - RollbackProcessorThrowExceptionDemo " + context.get("id"));
        int i = 1 / 0;
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollbackProcessorThrowExceptionDemo " + context.get("id"));
    }
}