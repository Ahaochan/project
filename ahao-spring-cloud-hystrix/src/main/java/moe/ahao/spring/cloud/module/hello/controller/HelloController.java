package moe.ahao.spring.cloud.module.hello.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
import moe.ahao.spring.cloud.module.hello.api.HelloApi;
import moe.ahao.spring.cloud.module.hello.feign.HelloFeignClient;
import moe.ahao.spring.cloud.module.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
public class HelloController implements HelloApi {
    public static final String KEY = "key";

    public static final ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

    @Autowired
    private HelloFeignClient helloFeignClient;

    @Autowired
    private HelloService helloService;

    public String hello() {
        RequestContextHolder.currentRequestAttributes().setAttribute(KEY, "RequestContextHolder value", RequestAttributes.SCOPE_REQUEST);
        threadLocal.set("threadLocal value");

        String hello = helloFeignClient.hello();
        String thread = helloService.thread();
        return "remote:" + hello + ", service: " + thread;
    }
}
