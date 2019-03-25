package com.ahao.spring.bean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.beans.Introspector;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 将当前类的包名下的所有用注解声明的 Bean, 以 包名+类名 的形式作为 BeanName
 * 包名前缀为2个分割符以内, 如本项目为: com.ahao
 */
public class PackageBeanNameGenerator extends AnnotationBeanNameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PackageBeanNameGenerator.class);

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = Introspector.decapitalize(definition.getBeanClassName());
        // 只初始化本项目的Bean
        if(StringUtils.startsWithIgnoreCase(beanClassName, getPackageNamePrefix())) {
            logger.debug("初始化Bean: " + beanClassName);
            return beanClassName;
        }
        return buildDefaultBeanName(definition);
    }

    /**
     * 取包名前缀, 限制为2个
     * 如本项目为: com.ahao
     * @return 包名前缀
     */
    private String getPackageNamePrefix() {
        String separator = ".";
        String[] splits = StringUtils.split(this.getClass().getPackage().getName(), separator);
        String prefix = Arrays.stream(splits).limit(2)
                .collect(Collectors.joining(separator));
        return prefix;
    }
}
