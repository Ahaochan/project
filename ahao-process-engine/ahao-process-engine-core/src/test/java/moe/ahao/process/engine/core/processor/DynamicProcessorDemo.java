package moe.ahao.process.engine.core.processor;

import lombok.NoArgsConstructor;
import moe.ahao.process.engine.core.process.DynamicProcessor;
import moe.ahao.process.engine.core.process.ProcessContext;

import java.util.List;

@NoArgsConstructor
public class DynamicProcessorDemo extends DynamicProcessor {
    public DynamicProcessorDemo(String name) {
        super(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行DynamicProcess" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        int id = context.get("id");
        return id % 2 == 0 ? "D" : "E";
    }
}

