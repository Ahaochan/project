package moe.ahao.spring.cloud.module.hello.api;

import org.springframework.web.bind.annotation.GetMapping;

public interface HelloApi {
    @GetMapping(value = "/hello")
    String hello();
}
