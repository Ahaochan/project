package moe.ahao.spring.cloud.zookeeper;

import moe.ahao.embedded.EmbeddedZookeeperTest;
import moe.ahao.spring.cloud.Starter;
import moe.ahao.spring.cloud.zookeeper.config.HelloApi;
import moe.ahao.spring.cloud.zookeeper.config.TestConfig;
import moe.ahao.util.spring.SpringContextHolder;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {SpringContextHolder.class, Starter.class, TestConfig.class, HelloApi.class})
@ActiveProfiles("test")
// TODO No instances available for ZOOKEEPER-CLIENT
public class ZookeeperTest extends EmbeddedZookeeperTest {
    public static final String applicationName = "ZOOKEEPER-CLIENT";

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("ZOOKEEPER-CLIENT");
        for (ServiceInstance serviceInstance : list) {
            System.out.println(serviceInstance.getUri().toString());
        }
    }

    // ================================ 使用 @LoadBalanced 做负载均衡  =================================================
    @Autowired
    private RestTemplate loadBalancedRestTemplate;
    @Test
    public void testLoadBalanced() throws Exception {
        String serverName = applicationName.toUpperCase();
        String msg = "测试";
        String response = loadBalancedRestTemplate.getForObject("http://" + serverName + "/hello?msg=" + msg, String.class);
        System.out.println(response);
        Assertions.assertEquals("hello world:" + msg, response);
    }
    // ================================ 使用 @LoadBalanced 做负载均衡  =================================================


    // ================================ 使用 LoadBalancerClient 做负载均衡  =================================================
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Test
    public void testLoadBalancerClient() {
        String serverName = applicationName.toUpperCase();
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();

        String msg = "测试";
        String response = restTemplate.getForObject("http://"+server.getHost()+":"+server.getPort()+"/hello?msg="+msg, String.class);
        System.out.println(response);
        Assertions.assertEquals("hello world:" + msg, response);
    }
    // ================================ 使用 LoadBalancerClient 做负载均衡  =================================================

    // ================================ 使用 Feign 做负载均衡  =================================================
    @Autowired
    private HelloApi feignClient;
    @Test
    public void test3() {
        String msg = "测试";
        String response = feignClient.hello(msg);
        System.out.println(response);
        Assertions.assertEquals("hello world:" + msg, response);
    }
    // ================================ 使用 Feign 做负载均衡  =================================================
}
