package moe.ahao.spring.cloud.module.hello.feign;

import feign.hystrix.FallbackFactory;
import moe.ahao.spring.cloud.module.hello.api.HelloApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name= "EUREKA-SERVER", fallbackFactory = HelloFeignClient.HelloApiHystrix.class)
public interface HelloFeignClient extends HelloApi {

    @Component
    class HelloApiHystrix implements FallbackFactory<HelloFeignClient> {
        private static final HelloFeignClient instance = new HelloFeignClient() {
            @Override
            public String hello() {
                return "请求失败, 从 Redis 获取缓存数据";
            }
        };

        @Override
        public HelloFeignClient create(Throwable throwable) {
            return instance;
        }
    }
}
