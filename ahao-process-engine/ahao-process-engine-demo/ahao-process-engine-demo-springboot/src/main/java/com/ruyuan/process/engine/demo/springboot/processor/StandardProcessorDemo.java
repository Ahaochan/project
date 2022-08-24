package com.ruyuan.process.engine.demo.springboot.processor;

import com.ruyuan.process.engine.demo.springboot.DemoService;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.process.StandardProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class StandardProcessorDemo extends StandardProcessor {

    @Autowired
    private DemoService demoService;

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("StandProcessor " + context.get("id"));
        demoService.sayHello();
    }
}

