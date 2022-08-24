package com.ruyuan.process.engine.demo;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.StandardProcessor;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class StandardProcessorDemo extends StandardProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println(Thread.currentThread().getName() + " - StandProcessor " + context.get("id"));
    }
}

