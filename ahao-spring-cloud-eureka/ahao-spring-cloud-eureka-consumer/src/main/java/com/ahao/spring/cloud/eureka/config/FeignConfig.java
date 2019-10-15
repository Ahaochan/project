package com.ahao.spring.cloud.eureka.config;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    // new一个form编码器，实现支持form表单提交
    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    // 开启Feign的日志
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}
