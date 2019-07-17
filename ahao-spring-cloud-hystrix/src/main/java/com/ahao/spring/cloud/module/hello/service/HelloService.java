package com.ahao.spring.cloud.module.hello.service;

import com.ahao.util.commons.lang.RandomHelper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public String hello() {
        try {
            Thread.sleep(RandomHelper.getInt(2000));
        } catch (InterruptedException ignored) {}
        return "hello";
    }


    public String helloHystrix() {
        return "helloHystrix";
    }

}
