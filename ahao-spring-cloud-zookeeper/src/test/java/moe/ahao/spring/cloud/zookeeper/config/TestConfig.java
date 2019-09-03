package moe.ahao.spring.cloud.zookeeper.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {
    @Bean
    @LoadBalanced // 负载均衡配置
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
