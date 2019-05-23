package com.ahao.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 使用 {@code @DependsOn(SpringContextHolder.BEAN_NAME)} 解决 applicationContext 为 null 的依赖问题
 */
@Service(SpringContextHolder.BEAN_NAME)
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    public static final String BEAN_NAME = "SpringContextHolder";
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext; // 违反 FindBugs 规范
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        applicationContext = null;
    }


    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new RuntimeException("applicationContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}

