package com.ahao.spring.cloud.module.hello.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "EUREKA-SERVER", fallback = HelloApiHystrix.class)
public interface HelloApi {
    @GetMapping(value = "/get")
    String hello();
}
