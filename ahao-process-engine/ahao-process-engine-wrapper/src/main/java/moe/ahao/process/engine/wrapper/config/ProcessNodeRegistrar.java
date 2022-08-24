package moe.ahao.process.engine.wrapper.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.model.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.model.ProcessModel;
import moe.ahao.process.engine.wrapper.parse.ClassPathXmlProcessParser;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcessNodeRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
    @Setter
    private BeanFactory beanFactory;

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

            // 2. 解析得到流程列表, 一个ProcessModel就是一个流程定义
            ClassPathXmlProcessParser classPathXmlProcessParser = new ClassPathXmlProcessParser(configFile);
            List<ProcessModel> processList = classPathXmlProcessParser.parse();

            // 3. 手动注册ProcessContextFactory的实例Bean
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
            bdb.addConstructorArgValue(new ArrayList<>(processList));
            bdb.addConstructorArgReference(ProcessorCreator.BEAN_NAME);
            if(beanDefinitionRegistry.containsBeanDefinition(ProcessStateStore.BEAN_NAME)) {
                bdb.addConstructorArgReference(ProcessStateStore.BEAN_NAME);
            }
            beanDefinitionRegistry.registerBeanDefinition(ProcessContextFactory.class.getName(), bdb.getBeanDefinition());
        } catch (Exception e) {
            log.error("ProcessNodeRegistrar register fail!", e);
            throw new BeanInitializationException("ProcessNodeRegistrar register fail!", e);
        }
    }
}
