package moe.ahao.process.engine.wrapper.instance;

import moe.ahao.process.engine.core.node.ProcessorNode;

/**
 * 流程节点实例化器
 */
public interface ProcessorCreator {
    String BEAN_NAME = "processorCreator";
    /**
     * 创建实例
     *
     * @param clazz         类
     * @param processorName 节点id
     * @return 实例化对象
     */
    ProcessorNode newInstance(Class<?> clazz, String processorName);
}
