package moe.ahao.process.engine.core.processor;

import lombok.NoArgsConstructor;
import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.process.RollbackProcessor;

import java.util.List;

@NoArgsConstructor
public class RollbackOneProcessorDemo extends RollbackProcessor {
    public RollbackOneProcessorDemo(String name) {
        super(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        int id = context.get("id");
        System.out.println("线程" + Thread.currentThread().getName() + " 执行RollbackProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
        if (id % 2 == 1) {
            throw new RuntimeException("id" + id + "不能是奇数");
        }
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 回滚执行RollbackProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }
}
