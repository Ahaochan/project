package com.ruyuan.process.engine.processor;

import com.ruyuan.process.engine.process.DynamicProcessor;
import com.ruyuan.process.engine.process.ProcessContext;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class DynamicProcessorDemo extends DynamicProcessor {

    private String id;
    private String nextId;

    public DynamicProcessorDemo(String id, String nextId) {
        this.id = id;
        this.nextId = nextId;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + id);
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return nextId;
    }
}
