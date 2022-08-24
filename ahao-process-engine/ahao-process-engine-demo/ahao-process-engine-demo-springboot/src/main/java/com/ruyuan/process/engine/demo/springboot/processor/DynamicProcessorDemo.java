package com.ruyuan.process.engine.demo.springboot.processor;


import com.ruyuan.process.engine.process.DynamicProcessor;
import com.ruyuan.process.engine.process.ProcessContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class DynamicProcessorDemo extends DynamicProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + context.get("id"));
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return "node4";
    }
}

