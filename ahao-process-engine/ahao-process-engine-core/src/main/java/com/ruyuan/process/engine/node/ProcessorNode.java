package com.ruyuan.process.engine.node;

import com.ruyuan.process.engine.enums.InvokeMethod;
import com.ruyuan.process.engine.process.DynamicProcessor;
import com.ruyuan.process.engine.process.Processor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个流程节点
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessorNode {

    /**
     * 流程节点名称
     */
    private String name;

    /**
     * 执行的动作
     */
    private Processor processor;

    /**
     * 下一个节点集合
     */
    private Map<String, ProcessorNode> nextNodes = new HashMap<>();

    /**
     * 调用下一个节点的方式c
     */
    private InvokeMethod invokeMethod = InvokeMethod.SYNC;

    /**
     * 是否已经存在同步的下一个节点
     */
    private boolean hasSyncNextNode = false;

    public void addNextNode(ProcessorNode processorNode) {
        if (processorNode.getName().equals(name)) {
            throw new IllegalArgumentException("Duplicate Node id: " + name);
        }
        if (nextNodes.containsKey(processorNode.getName())) {
            throw new IllegalArgumentException("Node[id=" + name + "] is already contains next node which id is " + processorNode.getName());
        }
        boolean isSync = InvokeMethod.SYNC.equals(processorNode.invokeMethod);
        boolean isDynamic = processor instanceof DynamicProcessor;
        if (!isDynamic && hasSyncNextNode && isSync) {
            throw new IllegalArgumentException("每个节点只能有一个同步调用的后继节点");
        }
        if (isSync) {
            hasSyncNextNode = true;
        }
        nextNodes.put(processorNode.getName(), processorNode);
    }

}
