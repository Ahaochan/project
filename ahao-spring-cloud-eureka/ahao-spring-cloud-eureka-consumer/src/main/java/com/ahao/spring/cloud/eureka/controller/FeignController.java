package com.ahao.spring.cloud.eureka.controller;

import com.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FeignController {
    private static final String serverName = EurekaConsumerApplication.serverName;
    // ================================ 使用 Feign 做负载均衡  =================================================
    // 注意要打开启动类的 @EnableFeignClients 注解
    @FeignClient(value = serverName) // 由客户端配置文件中的 spring.application.name 配置
    public interface SimpleFeignClient {
        // 需要 spring-cloud-starter-feign 依赖
        @GetMapping("/no-args") // 由@RequestMapping配置
        String noArgs();

        @RequestMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        String formData(@RequestParam String filename, @RequestPart("file") MultipartFile file);
    }
    @Autowired
    private SimpleFeignClient feignClient;

    @GetMapping("/no-args3")
    public String noArgs() {
        String response = feignClient.noArgs();
        return "Feign请求了: "+response;
    }

    @RequestMapping(value = "/form-data3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String formData(@RequestParam String filename, @RequestPart("file") MultipartFile file) {
        String response = feignClient.formData(filename, file);
        return "Feign请求了: "+response;
    }
    // ================================ 使用 Feign 做负载均衡  =================================================
}
