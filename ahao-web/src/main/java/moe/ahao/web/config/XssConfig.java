package moe.ahao.web.config;

import moe.ahao.spring.web.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XssConfig {
    @Bean
    public FilterRegistrationBean<XssFilter> hystrixCacheFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> bean = XssFilter.buildFilterBean("/*");
        return bean;
    }
}
