package moe.ahao.process.engine.core.processor;

import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.node.DynamicProcessorNode;

import java.util.List;

public class DynamicProcessorNodeDemo extends DynamicProcessorNode {
    public DynamicProcessorNodeDemo() {
    }
    public DynamicProcessorNodeDemo(String name) {
        this.setName(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行DynamicProcess" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }

    @Override
    public String nextNodeId(ProcessContext context) {
        int id = context.get("id");
        return id % 2 == 0 ? "D" : "E";
    }
}

