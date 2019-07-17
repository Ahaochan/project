package com.ahao.spring.cloud.hystrix.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@EnableCircuitBreaker
//@EnableHystrixDashboard
public class HystrixConfig {
}
