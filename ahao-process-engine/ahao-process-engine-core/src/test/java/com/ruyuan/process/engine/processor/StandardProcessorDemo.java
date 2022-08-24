package com.ruyuan.process.engine.processor;

import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.StandardProcessor;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class StandardProcessorDemo extends StandardProcessor {

    private String id;

    public StandardProcessorDemo(String id) {
        this.id = id;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("StandProcessor " + id);
    }
}
