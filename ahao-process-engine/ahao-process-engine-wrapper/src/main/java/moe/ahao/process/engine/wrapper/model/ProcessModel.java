package moe.ahao.process.engine.wrapper.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.definition.ProcessorDefinition;
import moe.ahao.process.engine.core.definition.ProcessorNodeDefinition;
import moe.ahao.process.engine.core.node.ProcessorNode;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 流程配置，表示一个完整的业务编排流程
 */
@Slf4j
public class ProcessModel {
    /**
     * 流程名称
     */
    @Getter
    private final String name;
    /**
     * 流程的所有节点，以节点名为key，以节点配置为value
     */
    private final Map<String, ProcessNodeModel> nodeModels;
    /**
     * 流程的起始节点
     */
    private ProcessNodeModel firstNodeModel;

    public ProcessModel(String name) {
        this.name = name;
        this.nodeModels = new HashMap<>();
    }

    /**
     * 添加一个节点
     *
     * @param nodeModel 节点配置
     */
    public void addNode(ProcessNodeModel nodeModel) {
        String nodeName = nodeModel.getName();
        if (nodeModels.containsKey(nodeName)) {
            throw new IllegalArgumentException("同一个流程不能定义多个相同id的节点, nodeName:" + nodeName);
        }
        nodeModels.put(nodeName, nodeModel);
        if (nodeModel.isBegin()) {
            if (firstNodeModel != null) {
                throw new IllegalArgumentException("同一个流程只能有一个开始节点");
            }
            this.firstNodeModel = nodeModel;
        }
    }

    public ProcessorDefinition buildDefinition(ProcessorCreator creator) {
        Map<String, ProcessorNodeDefinition> processorNodeMap = new HashMap<>();
        // 1. 第一次循环，将所有的流程节点配置ProcessNodeModel转化为ProcessorNode实例
        ProcessorNodeDefinition firstNode = null;
        for (ProcessNodeModel nodeModel : nodeModels.values()) {
            // 2.1. 获取业务逻辑处理的Processor
            Class<?> clazz = nodeModel.getClazz();
            ProcessorNode processor = creator.newInstance(clazz, nodeModel.getName());
            ProcessorNodeDefinition processorNode = new ProcessorNodeDefinition(nodeModel.getName(), processor);
            if (nodeModel.isBegin()) {
                // 2.2. 初始化起始节点, 在addNode()方法里已经进行了唯一性校验
                firstNode = processorNode;
            }
            // 2.3. 设置方法是异步还是同步
            processorNode.setInvokeMethod(nodeModel.getInvokeMethod());
            // 2.4. 将转化后的ProcessorNode实例缓存起来
            processorNodeMap.put(nodeModel.getName(), processorNode);
        }
        // 2. 第二次循环，为所有节点建立关联关系
        for (ProcessorNodeDefinition processNode : processorNodeMap.values()) {
            Set<String> nextNodeNames = nodeModels.get(processNode.getName()).getNextNodeNames();
            if (CollectionUtils.isEmpty(nextNodeNames)) {
                continue;
            }
            for (String nextNodeName : nextNodeNames) {
                if (!nodeModels.containsKey(nextNodeName)) {
                    throw new IllegalArgumentException("节点[name=" + nextNodeName + "]不存在");
                }
                // 将节点关联起来
                processNode.addNextNode(processorNodeMap.get(nextNodeName));
            }
        }
        // 3. 创建一个流程实例对象ProcessorDefinition
        ProcessorDefinition processorDefinition = new ProcessorDefinition.Builder(name, firstNode).build();
        return processorDefinition;
    }
}
