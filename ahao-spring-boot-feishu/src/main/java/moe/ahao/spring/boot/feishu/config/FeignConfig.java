package moe.ahao.spring.boot.feishu.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "moe.ahao.spring.boot.feishu.feign")
public class FeignConfig {
}
