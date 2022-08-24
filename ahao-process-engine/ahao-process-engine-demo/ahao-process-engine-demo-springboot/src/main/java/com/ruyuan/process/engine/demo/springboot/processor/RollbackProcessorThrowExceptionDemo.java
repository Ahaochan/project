package com.ruyuan.process.engine.demo.springboot.processor;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.RollbackProcessor;
import org.springframework.stereotype.Component;

/**
 * 6
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class RollbackProcessorThrowExceptionDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollbackProcessorThrowExceptionDemo " + context.get("id"));
        int i = 1 / 0;
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollbackProcessorThrowExceptionDemo " + context.get("id"));
    }
}