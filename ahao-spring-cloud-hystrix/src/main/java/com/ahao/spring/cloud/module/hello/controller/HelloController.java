package com.ahao.spring.cloud.module.hello.controller;

import com.ahao.spring.cloud.module.hello.api.HelloApi;
import com.ahao.spring.cloud.module.hello.service.HelloService;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
public class HelloController {
    public static final String KEY = "key";

    public static final ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

    @Autowired
    private HelloApi helloApi;

    @Autowired
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        RequestContextHolder.currentRequestAttributes().setAttribute(KEY, "RequestContextHolder value", RequestAttributes.SCOPE_REQUEST);
        threadLocal.set("threadLocal value");

        String hello = helloApi.hello();
        String thread = helloService.thread();
        return "remote:" + hello + ", service: " + thread;
    }
}
