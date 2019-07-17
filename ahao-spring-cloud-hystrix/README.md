# 简介
一个`Hytrix`熔断降级的`Demo`. 注意, `Hytrix`已经停更.

# 使用步骤
1. 启动[Eureka](../ahao-spring-cloud-eureka/src/main/java/com/ahao/spring/cloud/eureka/EurekaApplication.java)
1. 启动[本项目](./src/main/java/com/ahao/spring/cloud/Starter.java)
1. 访问[http://127.0.0.1:8080/hello](http://127.0.0.1:8080/hello), 正常输出
1. 停止`Eureka`
1. 访问[http://127.0.0.1:8080/hello](http://127.0.0.1:8080/hello), 输出熔断降级后的结果

# 熔断降级的两种实现
第一种是远程服务, 借助`Feign`, 关注[`HelloApi`](./src/main/java/com/ahao/spring/cloud/module/hello/api)
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

第二种是本地服务, 关注[`HelloService`](./src/main/java/com/ahao/spring/cloud/module/hello/service)
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

# 熔断降级的监控和集群监控
本`Demo`不涉及
