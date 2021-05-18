package moe.ahao.web.module.inventory.config;

import moe.ahao.web.module.inventory.listener.InitListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextListener;

@Configuration(proxyBeanMethods = false)
public class ProductInventoryConfig {
    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>();
        bean.setListener(new InitListener());
        return bean;
    }
}
