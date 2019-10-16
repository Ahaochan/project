# 简介
一个`Demo`, 提供了开箱即用的服务注册中心, 以及三种不同的客户端连接方式.

# 配置文件
客户端配置文件: [application-client.yml](./ahao-spring-cloud-eureka-consumer/src/main/resources/application-client.yml)
单注册中心配置文件: [application-server.yml](./ahao-spring-cloud-eureka-server/src/main/resources/application-server.yml)
多注册中心配置文件: [application-server-ha-1.yml](./ahao-spring-cloud-eureka-server/src/main/resources/application-server-ha-1.yml)、[application-server-ha-2.yml](./ahao-spring-cloud-eureka-server/src/main/resources/application-server-ha-2.yml)

# 三种不同的客户端连接方式
- `@LoadBalanced`修饰`RestTemplate`, 源码地址: [`LoadBalancerAnnotationController`](./ahao-spring-cloud-eureka-consumer/src/main/java/moe/ahao/spring/cloud/eureka/controller/LoadBalancerAnnotationController.java)
- 使用`LoadBalancerClient`获取**服务地址**, 再调用`RestTemplate`, 源码地址: [`LoadBalancerClientController`](./ahao-spring-cloud-eureka-consumer/src/main/java/moe/ahao/spring/cloud/eureka/controller/LoadBalancerClientController.java)
- 使用`Feign`框架做`RPC`, 源码地址: [`FeignController`](./ahao-spring-cloud-eureka-consumer/src/main/java/moe/ahao/spring/cloud/eureka/controller/FeignController.java)

# Feign 使用 form data 方式上传文件
要使用`Feign`提供的一个开源组件[`feign-form`](https://github.com/OpenFeign/feign-form).
在服务消费方配置`SpringFormEncoder`即可.
```java
@Configuration
@EnableFeignClients(basePackages = "moe.ahao")
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true")
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}

@FeignClient(value = serverName)
public interface SimpleFeignClient {
    @PostMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart("file") MultipartFile file);
}

@RestController
public class FeignController {
    @Autowired
    private SimpleFeignClient feignClient;

    @PostMapping(value = "/form-data3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart("file") MultipartFile file) {
        return feignClient.formData(param, json, file);
    }
}
```

有两个注意点
1. 使用`form data`不能`@RequestBody`和`@RequestPart`一起用, 如果要上传文件, 只能老老实实的把所有参数写在`Controller`上, 或者使用[本解决方案](https://stackoverflow.com/questions/21577782/).
2. [`feign-form`](https://github.com/OpenFeign/feign-form)的版本号一定要选对, 具体参考[官方文档](https://github.com/OpenFeign/feign-form#requirements).


# 单元测试
1. 启动[`Eureka`](./ahao-spring-cloud-eureka-server/src/main/java/moe/ahao/spring/cloud/eureka/EurekaServerApplication.java)服务端.
2. 启动[服务提供者](./ahao-spring-cloud-eureka-provider/src/main/java/moe/ahao/spring/cloud/eureka/EurekaProviderApplication.java).
3. 执行[单元测试](./ahao-spring-cloud-eureka-consumer/src/test/java/moe/ahao/spring/cloud/eureka/FeignTest.java)
