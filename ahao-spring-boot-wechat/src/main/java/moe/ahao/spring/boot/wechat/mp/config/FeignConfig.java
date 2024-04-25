package moe.ahao.spring.boot.wechat.mp.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(basePackages = "moe.ahao")
public class FeignConfig {
}
