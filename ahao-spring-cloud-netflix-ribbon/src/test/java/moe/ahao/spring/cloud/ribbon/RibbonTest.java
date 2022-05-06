package moe.ahao.spring.cloud.ribbon;

import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
// @ContextConfiguration(classes = RibbonConfig.class)
class RibbonTest {
    public static final String SERVICE_NAME = "service1";

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void serviceInstance() {
        ServiceInstance server1 = loadBalancerClient.choose(SERVICE_NAME);
        Assertions.assertEquals("www.baidu.com", server1.getHost());
        Assertions.assertEquals(80, server1.getPort());

        ServiceInstance server2 = loadBalancerClient.choose(SERVICE_NAME);
        Assertions.assertEquals("cn.bing.com", server2.getHost());
        Assertions.assertEquals(80, server2.getPort());
    }

    @Test
    void http() {
        String html = restTemplate.getForObject("http://" + SERVICE_NAME, String.class);
        System.out.println(html);
        Assertions.assertNotNull(html);
    }
}
