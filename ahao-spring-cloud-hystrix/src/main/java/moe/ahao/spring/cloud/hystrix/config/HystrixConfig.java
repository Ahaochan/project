package moe.ahao.spring.cloud.hystrix.config;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import moe.ahao.hystrix.TransmitHystrixConcurrencyStrategy;
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
