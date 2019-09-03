package moe.ahao.spring.cloud.zookeeper.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ZOOKEEPER-CLIENT")
public interface HelloApi {
    @GetMapping("/hello")
    String hello(@RequestParam String msg);
}
