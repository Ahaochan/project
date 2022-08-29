package moe.ahao.process.engine.core.processor;

import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.node.StandardProcessorNode;

import java.util.List;

public class StandardProcessorNodeDemo extends StandardProcessorNode {
    public StandardProcessorNodeDemo() {
    }
    public StandardProcessorNodeDemo(String name) {
        this.setName(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行StandProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }
}

