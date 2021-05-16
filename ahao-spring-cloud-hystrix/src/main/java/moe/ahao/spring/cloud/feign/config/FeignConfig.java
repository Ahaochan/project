package moe.ahao.spring.cloud.feign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("moe.ahao")
public class FeignConfig {
}
