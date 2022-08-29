package moe.ahao.process.engine.wrapper.config;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.store.ProcessStateDAO;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.parse.ClassPathXmlProcessParser;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Slf4j
public class ProcessNodeRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            // 1. 解析@EnableProcessEngine注解中的属性
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableProcessEngine.class.getName());
            if (annotationAttributes == null) {
                log.error("ProcessNodeRegistrar can not fount @EnableProcessEngine annotation attributes!");
                return;
            }
            String configFile = (String) annotationAttributes.get("value");

            // 2. 从配置里读取配置文件路径, 初始化一个配置解析器
            ClassPathXmlProcessParser parser = new ClassPathXmlProcessParser(configFile);

            // 3. 手动注册ProcessContextFactory的实例Bean
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
            bdb.addConstructorArgValue(parser);
            bdb.addConstructorArgReference(ProcessorCreator.BEAN_NAME);
            if(beanDefinitionRegistry.containsBeanDefinition(ProcessStateDAO.BEAN_NAME)) {
                bdb.addConstructorArgReference(ProcessStateDAO.BEAN_NAME);
            }
            beanDefinitionRegistry.registerBeanDefinition(ProcessContextFactory.class.getName(), bdb.getBeanDefinition());
        } catch (Exception e) {
            log.error("ProcessNodeRegistrar register fail!", e);
            throw new BeanInitializationException("ProcessNodeRegistrar register fail!", e);
        }
    }
}
