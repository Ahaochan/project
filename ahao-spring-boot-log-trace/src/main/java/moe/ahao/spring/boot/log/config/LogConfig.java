package moe.ahao.spring.boot.log.config;

import feign.Client;
import feign.Feign;
import moe.ahao.spring.boot.log.filter.TraceLogFeignInterceptor;
import moe.ahao.spring.boot.log.filter.TraceLogFilter;
import moe.ahao.spring.boot.log.thread.MDCThreadPoolExecutorAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class LogConfig {
    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean traceLogFilter() {
        return TraceLogFilter.buildFilterBean("/*", 1);
    }

    @Bean
    @ConditionalOnClass(Feign.class)
    @ConditionalOnBean(Client.class)
    public TraceLogFeignInterceptor traceLogFeignInterceptor() {
        return new TraceLogFeignInterceptor();
    }

    @Bean
    @ConditionalOnBean(Executor.class)
    public MDCThreadPoolExecutorAspect mdcThreadPoolExecutorAspect() {
        return new MDCThreadPoolExecutorAspect();
    }
}
