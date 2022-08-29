package moe.ahao.process.engine.core;

import moe.ahao.process.engine.core.definition.ProcessorDefinition;
import moe.ahao.process.engine.core.definition.ProcessorNodeDefinition;
import moe.ahao.process.engine.core.processor.DynamicProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.RollbackOneProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.RollbackTwoProcessorNodeDemo;
import moe.ahao.process.engine.core.processor.StandardProcessorNodeDemo;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProcessorTest {
    @Test
    @DisplayName("没有起始节点")
    void noFirst() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition.Builder("流程名称").build());
    }

    @Test
    @DisplayName("流程成环判断")
    void ring() {
        ProcessorNodeDefinition nodeA = new ProcessorNodeDefinition("A", new StandardProcessorNodeDemo("标准流程动作A"));
        ProcessorNodeDefinition nodeB = new ProcessorNodeDefinition("B", new StandardProcessorNodeDemo("标准流程动作B"));
        ProcessorNodeDefinition nodeC = new ProcessorNodeDefinition("C", new StandardProcessorNodeDemo("标准流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeA);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition.Builder("流程名称").setFirst(nodeA).build());
    }

    @Test
    @DisplayName("标准流程")
    void standard() {
        ProcessorNodeDefinition nodeA = new ProcessorNodeDefinition("A", new StandardProcessorNodeDemo("标准流程动作A"));
        ProcessorNodeDefinition nodeB = new ProcessorNodeDefinition("B", new StandardProcessorNodeDemo("标准流程动作B"));
        ProcessorNodeDefinition nodeC = new ProcessorNodeDefinition("C", new StandardProcessorNodeDemo("标准流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);

        ProcessorDefinition processorDefinition = new ProcessorDefinition.Builder("流程名称").setFirst(nodeA).build();
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition, new ProcessStateStore());
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("标准流程动作A", "标准流程动作B", "标准流程动作C");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("回滚流程")
    void rollback() {
        ProcessorNodeDefinition nodeA = new ProcessorNodeDefinition("A", new StandardProcessorNodeDemo("标准流程动作A"));
        ProcessorNodeDefinition nodeB = new ProcessorNodeDefinition("B", new RollbackOneProcessorNodeDemo("回滚流程动作B"));
        ProcessorNodeDefinition nodeC = new ProcessorNodeDefinition("C", new RollbackTwoProcessorNodeDemo("回滚流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);

        ProcessorDefinition processorDefinition = new ProcessorDefinition.Builder("流程名称").setFirst(nodeA).build();
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition, new ProcessStateStore());
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        Assertions.assertThrows(RuntimeException.class, context::start);
        List<String> expect = Arrays.asList("标准流程动作A", "回滚流程动作B", "回滚流程动作C", "回滚流程动作C", "回滚流程动作B");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("动态流程")
    void dynamic() {
        ProcessorNodeDefinition nodeA = new ProcessorNodeDefinition("A", new StandardProcessorNodeDemo("标准流程动作A"));
        ProcessorNodeDefinition nodeB = new ProcessorNodeDefinition("B", new StandardProcessorNodeDemo("标准流程动作B"));
        ProcessorNodeDefinition nodeC = new ProcessorNodeDefinition("C", new DynamicProcessorNodeDemo("动态流程动作C"));
        ProcessorNodeDefinition nodeD = new ProcessorNodeDefinition("D", new StandardProcessorNodeDemo("标准流程动作D"));
        ProcessorNodeDefinition nodeE = new ProcessorNodeDefinition("E", new StandardProcessorNodeDemo("标准流程动作E"));
        // A -> B -> C -> D,E
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeD);
        nodeC.addNextNode(nodeE);

        ProcessorDefinition processorDefinition = new ProcessorDefinition.Builder("流程名称").setFirst(nodeA).build();
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition, new ProcessStateStore());
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("标准流程动作A", "标准流程动作B", "动态流程动作C", "标准流程动作D");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }
}
