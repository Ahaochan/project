package com.ahao.spring.cloud.eureka.controller;

import com.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {
    private static final String serverName = EurekaConsumerApplication.serverName;
    // ================================ 使用 Feign 做负载均衡  =================================================
    // 注意要打开启动类的 @EnableFeignClients 注解
    @FeignClient(serverName) // 由客户端配置文件中的 spring.application.name 配置
    public interface SimpleFeignClient {
        // 需要 spring-cloud-starter-feign 依赖
        @GetMapping("/no-args") // 由@RequestMapping配置
        String noArgs();
    }
    @Autowired
    private SimpleFeignClient feignClient;

    @GetMapping("/no-args3")
    public String test3() {
        String response = feignClient.noArgs();
        return "Feign请求了: "+response;
    }
    // ================================ 使用 Feign 做负载均衡  =================================================
}
