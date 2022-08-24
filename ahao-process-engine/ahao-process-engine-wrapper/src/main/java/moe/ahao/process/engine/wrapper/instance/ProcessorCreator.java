package moe.ahao.process.engine.wrapper.instance;

import moe.ahao.process.engine.core.process.Processor;

/**
 * 流程节点实例化器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface ProcessorCreator {
    /**
     * 创建实例
     *
     * @param clazz         类
     * @param processorName 节点id
     * @return 实例化对象
     */
    Processor newInstance(Class<?> clazz, String processorName) throws ReflectiveOperationException;
}
