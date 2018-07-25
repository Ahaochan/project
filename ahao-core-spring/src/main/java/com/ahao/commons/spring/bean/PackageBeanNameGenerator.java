package com.ahao.commons.spring.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.beans.Introspector;

/**
 * 初始化Bean名字为 包名.类名, 避免 BeanName 冲突
 */
public class PackageBeanNameGenerator implements BeanNameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PackageBeanNameGenerator.class);

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = Introspector.decapitalize(definition.getBeanClassName());
        logger.debug("初始化Bean: " + beanClassName);
        return beanClassName;
    }
}
