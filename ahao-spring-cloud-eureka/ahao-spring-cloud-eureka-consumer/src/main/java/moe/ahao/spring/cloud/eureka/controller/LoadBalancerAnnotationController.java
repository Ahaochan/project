package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 @LoadBalanced 做负载均衡
 */
@RestController
public class LoadBalancerAnnotationController {
    @Bean
    @LoadBalanced // 负载均衡配置
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/no-args1")
    public String noArgs() {
        // http://服务名/请求路径
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true
        String serverName = EurekaConsumerApplication.serverName;
        String response = restTemplate.getForObject("http://"+serverName+"/no-args", String.class);
        return "RestTemplate请求了"+serverName+": "+response;
    }
}
