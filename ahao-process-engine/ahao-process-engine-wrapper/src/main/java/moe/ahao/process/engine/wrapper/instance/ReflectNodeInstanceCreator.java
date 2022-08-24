package moe.ahao.process.engine.wrapper.instance;

import moe.ahao.process.engine.core.process.Processor;

/**
 * 通过反射创建流程处理器Processor
 */
public class ReflectNodeInstanceCreator implements ProcessorCreator {

    @Override
    public Processor newInstance(Class<?> clazz, String processorName) throws ReflectiveOperationException {
        // 1. 通过反射创建出Processor的实例对象
        Object o = clazz.newInstance();
        if (!(o instanceof Processor)) {
            throw new IllegalArgumentException("类" + clazz.getName() + "不是Processor实例");
        }
        // 2. 初始化name参数
        Processor processor = (Processor) o;
        processor.setName(processorName);
        return processor;
    }
}
