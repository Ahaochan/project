package moe.ahao.process.engine.core.definition;

import lombok.Data;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.core.node.DynamicProcessorNode;
import moe.ahao.process.engine.core.node.ProcessorNode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义每个流程节点
 */
@Data
public class ProcessorNodeDefinition {
    /**
     * 流程节点名称
     */
    private String name;
    /**
     * 流程节点执行的动作
     */
    private ProcessorNode processorNode;
    /**
     * 下一个节点集合
     */
    private Map<String, ProcessorNodeDefinition> nextNodes = new HashMap<>();
    /**
     * 调用下一个节点的调用方式
     */
    private InvokeMethod invokeMethod = InvokeMethod.SYNC;
    /**
     * 是否已经存在同步的下一个节点
     */
    private boolean hasSyncNextNode = false;

    public ProcessorNodeDefinition(String name, ProcessorNode processorNode) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Processor Node name missing.");
        }
        this.name = name;
        this.processorNode = processorNode;
    }

    public void addNextNode(ProcessorNodeDefinition definition) {
        // 1. 重复节点的校验
        if (definition.getName().equals(name)) {
            throw new IllegalArgumentException("Duplicate Node name: " + name);
        }
        if (nextNodes.containsKey(definition.getName())) {
            throw new IllegalArgumentException("Node[id=" + name + "] is already contains next node which id is " + definition.getName());
        }

        // 2. 保证每个节点只能有一个同步调用的后继节点
        boolean isSync = InvokeMethod.SYNC.equals(definition.invokeMethod);
        boolean isDynamic = this.processorNode instanceof DynamicProcessorNode;
        if (!isDynamic && hasSyncNextNode && isSync) {
            throw new IllegalArgumentException("每个节点只能有一个同步调用的后继节点");
        }
        if (isSync) {
            hasSyncNextNode = true;
        }
        // 3. 将节点加入后继节点集合内
        nextNodes.put(definition.getName(), definition);
    }
}
