package com.ahao.spring.cloud.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Client {
    @GetMapping("/get")
    public String get() {
        return "请求成功";
    }

    @Value("${spring.application.name}")
    private String applicationName;

    // ================================ 使用 @LoadBalanced 做负载均衡  =================================================
    @Bean
    @LoadBalanced // 负载均衡配置
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/test1")
    public String test1() {
        // http://服务名/请求路径
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true
        String serverName = applicationName.toUpperCase();
        String response = restTemplate.getForObject("http://"+serverName+"/get", String.class);
        return "RestTemplate请求了"+serverName+": "+response;
    }
    // ================================ 使用 @LoadBalanced 做负载均衡  =================================================


    // ================================ 使用 LoadBalancerClient 做负载均衡  =================================================
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @GetMapping("/test2")
    public String test2() {
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true

        String serverName = applicationName.toUpperCase();
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://"+server.getHost()+":"+server.getPort()+"/get", String.class);
        return "LoadBalancerClient请求了"+serverName+": "+response;
    }
    // ================================ 使用 LoadBalancerClient 做负载均衡  =================================================

    // ================================ 使用 Feign 做负载均衡  =================================================
    // 注意要打开启动类的 @EnableFeignClients 注解
    @FeignClient("EUREKA") // 由客户端配置文件中的 spring.application.name 配置
    public interface SimpleFeignClient {
        // 需要 spring-cloud-starter-feign 依赖
        @GetMapping("/get") // 由@RequestMapping配置
        String getTest();
    }
    @Autowired
    private SimpleFeignClient feignClient;

    @GetMapping("/test3")
    public String test3() {
        String response = feignClient.getTest();
        return "Feign请求了: "+response;
    }
    // ================================ 使用 Feign 做负载均衡  =================================================
}
