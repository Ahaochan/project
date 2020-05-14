package com.ahao.spring.cloud.hystrix.config;

import com.ahao.spring.cloud.hystrix.concurrency.TransmitHystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@EnableCircuitBreaker
//@EnableHystrixDashboard
public class HystrixConfig {
    @Bean
    public HystrixConcurrencyStrategy hystrixConcurrencyStrategy() {
        return new TransmitHystrixConcurrencyStrategy();
    }
}
