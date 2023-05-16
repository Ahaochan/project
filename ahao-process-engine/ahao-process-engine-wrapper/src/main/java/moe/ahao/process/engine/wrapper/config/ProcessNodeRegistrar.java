package moe.ahao.process.engine.wrapper.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.store.ProcessStateDAO;
import moe.ahao.process.engine.wrapper.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.enums.XmlReadFromEnum;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.parse.ProcessParser;
import moe.ahao.process.engine.wrapper.parse.ProcessParserFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Slf4j
public class ProcessNodeRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    @Setter
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            // 1. 解析@EnableProcessEngine注解中的属性
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableProcessEngine.class.getName());
            if (annotationAttributes == null) {
                log.error("ProcessNodeRegistrar can not fount @EnableProcessEngine annotation attributes!");
                return;
            }
            String configFile = (String) annotationAttributes.get(EnableProcessEngine.VALUE_KEY);
            XmlReadFromEnum readFrom = (XmlReadFromEnum) annotationAttributes.get(EnableProcessEngine.READ_FROM_KEY);

            // 2. 从配置里读取配置文件路径, 初始化一个配置解析器
            ProcessParser parser = new ProcessParserFactory(environment).create(configFile, readFrom);

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
