package com.ruyuan.process.engine;

import com.ruyuan.process.engine.node.ProcessorDefinition;
import com.ruyuan.process.engine.node.ProcessorNode;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.processor.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class ProcessorDefinitionTest {

    @Test
    public void testHasRing() {
        ProcessorNode nodeA = new ProcessorNode();
        ProcessorNode nodeB = new ProcessorNode();
        ProcessorNode nodeC = new ProcessorNode();
        ProcessorNode nodeD = new ProcessorNode();
        ProcessorNode nodeE = new ProcessorNode();
        nodeA.setName("A");
        nodeB.setName("B");
        nodeC.setName("C");
        nodeD.setName("D");
        nodeE.setName("E");
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeA);
        assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition(nodeA));
    }

    @Test
    public void testProcessContext() {
        ProcessorNode nodeA = new ProcessorNode();
        ProcessorNode nodeB = new ProcessorNode();
        ProcessorNode nodeC = new ProcessorNode();
        ProcessorNode nodeD = new ProcessorNode();
        ProcessorNode nodeE = new ProcessorNode();
        nodeA.setName("A");
        nodeA.setProcessor(new StandardProcessorDemo("A"));

        nodeB.setName("B");
        nodeB.setProcessor(new RollBackProcessorDemo("B", false));

        nodeC.setName("C");
        nodeC.setProcessor(new DynamicProcessorDemo("C", "D"));

        nodeD.setName("D");
        nodeD.setProcessor(new RollBackProcessorDemo("D", true));

        nodeE.setName("E");
        nodeE.setProcessor(new StandardProcessorDemo("E"));

        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeD);
        nodeC.addNextNode(nodeE);

        ProcessorDefinition processorDefinition = new ProcessorDefinition(nodeA);
        ProcessContext context = new ProcessContext("1", processorDefinition);
        context.start();
    }


}
