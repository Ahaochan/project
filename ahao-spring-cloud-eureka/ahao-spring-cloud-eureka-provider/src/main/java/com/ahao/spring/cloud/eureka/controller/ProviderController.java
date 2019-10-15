package com.ahao.spring.cloud.eureka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @GetMapping("/no-args")
    public String noArgs() {
        return "无参方法请求成功";
    }
}
