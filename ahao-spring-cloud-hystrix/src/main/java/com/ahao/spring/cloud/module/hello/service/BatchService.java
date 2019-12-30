package com.ahao.spring.cloud.module.hello.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Hystrix 实现请求合并, 不支持 Feign
 */
@Service
public class BatchService {
    @HystrixCollapser(batchMethod = "findAll",
        collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "200"), // 单个请求的延迟时间
            @HystrixProperty(name = "maxRequestsInBatch", value = "50"),        // 允许最大的合并请求数量
            @HystrixProperty(name = "requestCache.enabled", value = "false")    // 是否允许开启请求的本地缓存
        })
    public Future<String> find(Integer id) {
        return null;
    }

    @HystrixCommand
    public List<String> findAll(List<Integer> ids) {
        return ids.stream().map(i -> "name" + i).collect(Collectors.toList());
    }
}
