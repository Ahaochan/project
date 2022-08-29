package moe.ahao.process.engine.core.processor;

import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.node.RollbackProcessorNode;

import java.util.List;

public class RollbackTwoProcessorNodeDemo extends RollbackProcessorNode {
    public RollbackTwoProcessorNodeDemo() {
    }
    public RollbackTwoProcessorNodeDemo(String name) {
        this.setName(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        int id = context.get("id");
        System.out.println("线程" + Thread.currentThread().getName() + " 执行RollbackProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
        if (id % 2 == 0) {
            throw new RuntimeException("id" + id + "不能是偶数");
        }
    }

    @Override
    public void rollback(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 回滚执行RollbackProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }
}
