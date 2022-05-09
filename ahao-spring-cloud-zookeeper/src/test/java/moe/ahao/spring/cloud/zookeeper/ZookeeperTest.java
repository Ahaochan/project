package moe.ahao.spring.cloud.zookeeper;

import moe.ahao.embedded.ZookeeperExtension;
import moe.ahao.spring.cloud.Starter;
import moe.ahao.spring.cloud.zookeeper.config.HelloApi;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test")
// @ContextConfiguration(classes = {SpringContextHolder.class, Starter.class, TestConfig.class, HelloApi.class})
class ZookeeperTest {
    @RegisterExtension
    static ZookeeperExtension zookeeperExtension = new ZookeeperExtension();

    public static final String applicationName = "ZOOKEEPER-CLIENT";

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private DiscoveryClient discoveryClient;

    @BeforeEach
    void serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("ZOOKEEPER-CLIENT");
        Assumptions.assumeTrue(list.size() > 0, "必须先启动Starter, 并修改配置文件中的ZK地址");
        for (ServiceInstance serviceInstance : list) {
            System.out.println(serviceInstance.getUri().toString());
        }
    }

    // ================================ 使用 @LoadBalanced 做负载均衡  =================================================
    @Autowired
    private RestTemplate loadBalancedRestTemplate;
    @Test
    void testLoadBalanced() throws Exception {
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
    void testLoadBalancerClient() {
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
    void test3() {
        String msg = "测试";
        String response = feignClient.hello(msg);
        System.out.println(response);
        Assertions.assertEquals("hello world:" + msg, response);
    }
    // ================================ 使用 Feign 做负载均衡  =================================================
}
