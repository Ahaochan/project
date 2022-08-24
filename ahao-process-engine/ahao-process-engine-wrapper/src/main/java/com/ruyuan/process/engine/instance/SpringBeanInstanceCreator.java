package com.ruyuan.process.engine.instance;

import com.ruyuan.process.engine.model.ProcessContextFactory;
import com.ruyuan.process.engine.process.Processor;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class SpringBeanInstanceCreator implements ProcessorInstanceCreator {

    @Override
    public Processor newInstance(String className, String name) throws Exception {
        Object bean;
        try {
            Class<?> clazz = Class.forName(className);
            bean = SpringBeanUtil.getBean(clazz);
        } catch (BeansException e) {
            return ProcessContextFactory.DEFAULT_INSTANCE_CREATOR.newInstance(className, name);
        }
        if (!(bean instanceof Processor)) {
            throw new IllegalArgumentException("类" + className + "不是Processor实例");
        }
        Processor processor = (Processor) bean;
        processor.setName(name);
        return (Processor) bean;
    }
}
