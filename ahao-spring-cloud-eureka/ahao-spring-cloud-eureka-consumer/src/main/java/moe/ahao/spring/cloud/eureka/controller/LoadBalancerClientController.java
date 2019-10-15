package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 LoadBalancerClient 做负载均衡
 */
@RestController
public class LoadBalancerClientController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/no-args2")
    public String noArgs() {
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true
        String serverName = EurekaConsumerApplication.serverName;
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://"+server.getHost()+":"+server.getPort()+"/no-args", String.class);
        return "LoadBalancerClient请求了"+serverName+": "+response;
    }
}
