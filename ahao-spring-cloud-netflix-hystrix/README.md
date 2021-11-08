# 简介
一个`Hytrix`熔断降级的`Demo`. 注意, `Hytrix`已经停止开发, 但还在维护中.

# 使用步骤
1. 启动[本项目](ahao-spring-cloud-hystrix/src/main/java/moe/ahao/spring/cloud/Starter.java)
1. 访问[http://127.0.0.1:8080/hello](http://127.0.0.1:8080/hello), 输出熔断降级后的结果

# 熔断降级的两种实现
第一种是远程服务, 借助`Feign`, 关注[`HelloApi`](ahao-spring-cloud-hystrix/src/main/java/moe/ahao/spring/cloud/module/hello/api)
```java
@FeignClient(name= "EUREKA-SERVER", fallback = HelloApiHystrix.class)
public interface HelloApi {
    @GetMapping(value = "/get")
    String hello();
}
```
`fallback`属性指定了熔断降级后的处理类, 该类必须被`Spring`管理, 一般是从缓存中获取数据.
```java
@Component
public class HelloApiHystrix implements HelloApi {
    @Override
    public String hello() {
        return "请求失败, 从 Redis 获取缓存数据";
    }
}
```

第二种是本地服务, 关注[`HelloService`](ahao-spring-cloud-hystrix/src/main/java/moe/ahao/spring/cloud/module/hello/service)
```java
@Service
public class HelloService {
    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public String hello() {
        try {
            Thread.sleep(RandomHelper.getInt(2000));
        } catch (InterruptedException e) {}
        return "hello";
    }
    public String helloHystrix() {
        return "helloHystrix";
    }
}
```
很容易看出, `hello()`直接抛出异常后, 会调用`helloHystrix()`方法.

# 线程和信号量限流
`Hystrix`还提供了限流的功能, 设置`execution.isolation.strategy`限流策略就可以用了.
```java
@Service
public class HelloService {
    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
    },
        threadPoolKey = "threadPoolKey",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "10"),
            @HystrixProperty(name = "maxQueueSize", value = "2000"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "30"),
        })
    public String thread() {
        return "thread";
    }
    
    @HystrixCommand(fallbackMethod = "helloHystrix", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
        @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10")
    })
    public String semaphore() {
        return "semaphore";
    }
}
```

# 熔断降级的监控和集群监控
本`Demo`不涉及
