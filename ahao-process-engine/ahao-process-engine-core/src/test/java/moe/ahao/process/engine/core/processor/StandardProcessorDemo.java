package moe.ahao.process.engine.core.processor;

import lombok.NoArgsConstructor;
import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.process.StandardProcessor;

import java.util.List;

@NoArgsConstructor
public class StandardProcessorDemo extends StandardProcessor {
    public StandardProcessorDemo(String name) {
        super(name);
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行StandProcessor" + this.getName() + ", id:" + context.get("id"));
        ((List<String>)context.get("path")).add(this.getName());
    }
}

