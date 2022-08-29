package moe.ahao.process.engine.wrapper.instance;

import lombok.Setter;
import moe.ahao.process.engine.core.node.ProcessorNode;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 通过Spring容器获取流程处理器Processor
 */
public class SpringBeanInstanceCreator implements ProcessorCreator, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    @Override
    public ProcessorNode newInstance(Class<?> clazz, String name) {
        // 1. 通过Spring容器获取Processor的实例对象
        Object bean = this.getBean(clazz, name);
        if (!(bean instanceof ProcessorNode)) {
            throw new IllegalArgumentException("类" + clazz.getName() + "不是Processor实例");
        }
        // 2. 初始化name参数
        ProcessorNode processor = (ProcessorNode) bean;
        processor.setName(name);
        return (ProcessorNode) bean;
    }

    private Object getBean(Class<?> clazz, String name) {
        Map<String, ?> beansOfType = applicationContext.getBeansOfType(clazz);
        if (beansOfType.size() == 0) {
            throw new NoSuchBeanDefinitionException(clazz, "节点[" + name + "]的实现类" + clazz + "没有注册为Spring Bean");
        } else if (beansOfType.size() > 1) {
            throw new NoUniqueBeanDefinitionException(clazz, beansOfType.keySet());
        } else {
            Map.Entry<String, ?> entry = beansOfType.entrySet().stream().findAny().get();
            String beanName = entry.getKey();
            if (applicationContext.isPrototype(beanName)) {
                return entry.getValue();
            } else {
                throw new BeanCreationNotAllowedException(beanName, "节点[" + name + "]的实现类" + clazz + "的Scope必须为prototype");
            }
        }
    }
}
