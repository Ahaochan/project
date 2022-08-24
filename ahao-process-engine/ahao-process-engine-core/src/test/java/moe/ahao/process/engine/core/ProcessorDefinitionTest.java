package moe.ahao.process.engine.core;

import moe.ahao.process.engine.core.node.ProcessorDefinition;
import moe.ahao.process.engine.core.node.ProcessorNode;
import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.processor.DynamicProcessorDemo;
import moe.ahao.process.engine.core.processor.RollbackOneProcessorDemo;
import moe.ahao.process.engine.core.processor.RollbackTwoProcessorDemo;
import moe.ahao.process.engine.core.processor.StandardProcessorDemo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProcessorDefinitionTest {
    @Test
    @DisplayName("没有起始节点")
    void noFirst() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition("流程名称", null));
    }

    @Test
    @DisplayName("流程成环判断")
    void ring() {
        ProcessorNode nodeA = new ProcessorNode("A", new StandardProcessorDemo("标准流程动作A"));
        ProcessorNode nodeB = new ProcessorNode("B", new StandardProcessorDemo("标准流程动作B"));
        ProcessorNode nodeC = new ProcessorNode("C", new StandardProcessorDemo("标准流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeA);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition("流程名称", nodeA));
    }

    @Test
    @DisplayName("标准流程")
    void standard() {
        ProcessorNode nodeA = new ProcessorNode("A", new StandardProcessorDemo("标准流程动作A"));
        ProcessorNode nodeB = new ProcessorNode("B", new StandardProcessorDemo("标准流程动作B"));
        ProcessorNode nodeC = new ProcessorNode("C", new StandardProcessorDemo("标准流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);

        ProcessorDefinition processorDefinition = new ProcessorDefinition("流程名称", nodeA);
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition);
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("标准流程动作A", "标准流程动作B", "标准流程动作C");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("回滚流程")
    void rollback() {
        ProcessorNode nodeA = new ProcessorNode("A", new StandardProcessorDemo("标准流程动作A"));
        ProcessorNode nodeB = new ProcessorNode("B", new RollbackOneProcessorDemo("回滚流程动作B"));
        ProcessorNode nodeC = new ProcessorNode("C", new RollbackTwoProcessorDemo("回滚流程动作C"));
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);

        ProcessorDefinition processorDefinition = new ProcessorDefinition("流程名称", nodeA);
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition);
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        Assertions.assertThrows(RuntimeException.class, context::start);
        List<String> expect = Arrays.asList("标准流程动作A", "回滚流程动作B", "回滚流程动作C", "回滚流程动作C", "回滚流程动作B");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    @Test
    @DisplayName("动态流程")
    void dynamic() {
        ProcessorNode nodeA = new ProcessorNode("A", new StandardProcessorDemo("标准流程动作A"));
        ProcessorNode nodeB = new ProcessorNode("B", new StandardProcessorDemo("标准流程动作B"));
        ProcessorNode nodeC = new ProcessorNode("C", new DynamicProcessorDemo("动态流程动作C"));
        ProcessorNode nodeD = new ProcessorNode("D", new StandardProcessorDemo("标准流程动作D"));
        ProcessorNode nodeE = new ProcessorNode("E", new StandardProcessorDemo("标准流程动作E"));
        // A -> B -> C -> D,E
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeD);
        nodeC.addNextNode(nodeE);

        ProcessorDefinition processorDefinition = new ProcessorDefinition("流程名称", nodeA);
        ProcessContext context = new ProcessContext("全局唯一id", processorDefinition);
        context.set("id", 123456);
        context.set("path", new ArrayList<>());

        context.start();
        List<String> expect = Arrays.asList("标准流程动作A", "标准流程动作B", "动态流程动作C", "标准流程动作D");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }
}
