package moe.ahao.spring.cloud.module.hello.feign;

import moe.ahao.spring.cloud.module.hello.api.HelloApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(url= "http://aaa.com", name = "test", fallback = HelloFeignClient.HelloApiHystrix.class)
public interface HelloFeignClient extends HelloApi {
    @Component
    class HelloApiHystrix implements HelloFeignClient {
        @Override
        public String hello() {
            return "请求失败, 从 Redis 获取缓存数据";
        }
    }
}
