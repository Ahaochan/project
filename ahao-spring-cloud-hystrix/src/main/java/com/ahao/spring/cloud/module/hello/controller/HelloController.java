package com.ahao.spring.cloud.module.hello.controller;

import com.ahao.spring.cloud.module.hello.api.HelloApi;
import com.ahao.spring.cloud.module.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloApi helloApi;

    @Autowired
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        return "remote:" + helloApi.hello() + ", service: " + helloService.hello();
    }
}
