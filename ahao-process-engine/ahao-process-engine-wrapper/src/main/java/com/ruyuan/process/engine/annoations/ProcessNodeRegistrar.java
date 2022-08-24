package com.ruyuan.process.engine.annoations;

import com.ruyuan.process.engine.annoations.EnableProcessEngine;
import com.ruyuan.process.engine.config.ClassPathXmlProcessParser;
import com.ruyuan.process.engine.model.ProcessContextFactory;
import com.ruyuan.process.engine.model.ProcessModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class ProcessNodeRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            String configFile = (String) annotationMetadata.getAnnotationAttributes(EnableProcessEngine.class.getName())
                    .get("value");
            // 1. 解析得到流程
            ClassPathXmlProcessParser classPathXmlProcessParser = new ClassPathXmlProcessParser(configFile);
            List<ProcessModel> processLists = classPathXmlProcessParser.parse();

            // 2. 注册ProcessContextFactory
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
            bdb.addConstructorArgValue(new ArrayList<>(processLists));
            bdb.addConstructorArgReference("springBeanInstanceCreator");
            bdb.addConstructorArgReference("processStateStore");
            beanDefinitionRegistry.registerBeanDefinition(ProcessContextFactory.class.getName(), bdb.getBeanDefinition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
