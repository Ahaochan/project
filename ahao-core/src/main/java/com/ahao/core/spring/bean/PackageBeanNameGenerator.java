package com.ahao.core.spring.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.beans.Introspector;

public class PackageBeanNameGenerator implements BeanNameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PackageBeanNameGenerator.class);

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = Introspector.decapitalize(definition.getBeanClassName());
        logger.debug("装载Bean: " + beanClassName);
        return beanClassName;
    }
}
