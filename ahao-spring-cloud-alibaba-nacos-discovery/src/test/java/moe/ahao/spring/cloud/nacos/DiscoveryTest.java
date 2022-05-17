package moe.ahao.spring.cloud.nacos;

import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class DiscoveryTest {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    void serviceUrl() {
        List<String> services = discoveryClient.getServices();
        System.out.println("服务数量:" + services.size());
        for (String service : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance serviceInstance : instances) {
                System.out.println(serviceInstance.getUri().toString());
            }
        }
        Assumptions.assumeTrue(services.size() > 0, "必须先启动Eureka和Provider, 按照Readme.md文档进行测试");
    }
}
