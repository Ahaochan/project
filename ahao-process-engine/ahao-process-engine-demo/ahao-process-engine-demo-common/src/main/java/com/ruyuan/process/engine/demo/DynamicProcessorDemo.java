package com.ruyuan.process.engine.demo;


import com.ruyuan.process.engine.process.DynamicProcessor;
import com.ruyuan.process.engine.process.ProcessContext;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class DynamicProcessorDemo extends DynamicProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + context.get("id"));
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return context.get("nextId");
    }
}

