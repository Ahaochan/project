package com.ahao.spring.cloud.module.hello.api;

import org.springframework.stereotype.Component;

@Component
public class HelloApiHystrix implements HelloApi {

    @Override
    public String hello() {
        return "请求失败, 从 Redis 获取缓存数据";
    }
}
