package com.ahao.spring.cloud.eureka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true")
public class FeignConfig {
}
