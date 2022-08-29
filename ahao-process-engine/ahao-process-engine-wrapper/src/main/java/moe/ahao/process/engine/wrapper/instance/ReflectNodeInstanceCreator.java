package moe.ahao.process.engine.wrapper.instance;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.node.ProcessorNode;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * 通过反射创建流程处理器Processor
 */
@Slf4j
public class ReflectNodeInstanceCreator implements ProcessorCreator {
    @Override
    public ProcessorNode newInstance(Class<?> clazz, String processorName) {
        try {
            // 1. 通过反射创建出Processor的实例对象
            Object o = clazz.newInstance();
            if (!(o instanceof ProcessorNode)) {
                throw new IllegalArgumentException("类" + clazz.getName() + "不是Processor实例");
            }
            // 2. 初始化name参数
            ProcessorNode processor = (ProcessorNode) o;
            processor.setName(processorName);
            return processor;
        } catch (ReflectiveOperationException e) {
            throw new BeanInitializationException("类" + clazz.getName() + "初始化失败", e);
        }
    }
}
