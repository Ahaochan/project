package moe.ahao.process.engine.core.node;

import lombok.Data;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.core.process.DynamicProcessor;
import moe.ahao.process.engine.core.process.Processor;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个流程节点
 */
@Data
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
     * 调用下一个节点的调用方式
     */
    private InvokeMethod invokeMethod = InvokeMethod.SYNC;

    /**
     * 是否已经存在同步的下一个节点
     */
    private boolean hasSyncNextNode = false;

    public ProcessorNode(String name, Processor processor) {
        this.name = name;
        this.processor = processor;
    }

    public void addNextNode(ProcessorNode processorNode) {
        // 1. 重复节点的校验
        if (processorNode.getName().equals(name)) {
            throw new IllegalArgumentException("Duplicate Node id: " + name);
        }
        if (nextNodes.containsKey(processorNode.getName())) {
            throw new IllegalArgumentException("Node[id=" + name + "] is already contains next node which id is " + processorNode.getName());
        }
        // 2. 保证每个节点只能有一个同步调用的后继节点
        boolean isSync = InvokeMethod.SYNC.equals(processorNode.invokeMethod);
        boolean isDynamic = processor instanceof DynamicProcessor;
        if (!isDynamic && hasSyncNextNode && isSync) {
            throw new IllegalArgumentException("每个节点只能有一个同步调用的后继节点");
        }
        if (isSync) {
            hasSyncNextNode = true;
        }
        // 3. 将节点加入后继节点集合内
        nextNodes.put(processorNode.getName(), processorNode);
    }
}
