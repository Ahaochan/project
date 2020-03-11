package moe.ahao.spring.cloud.ribbon;

import moe.ahao.spring.cloud.ribbon.config.RibbonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RibbonConfig.class})
@EnableAutoConfiguration
public class RibbonTest {
    public static final String SERVICE_NAME = "service1";

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void serviceInstance() {
        ServiceInstance server1 = loadBalancerClient.choose(SERVICE_NAME);
        Assertions.assertEquals("www.baidu.com", server1.getHost());
        Assertions.assertEquals(80, server1.getPort());

        ServiceInstance server2 = loadBalancerClient.choose(SERVICE_NAME);
        Assertions.assertEquals("www.bing.com", server2.getHost());
        Assertions.assertEquals(80, server2.getPort());
    }

    @Test
    public void http() {
        String html = restTemplate.getForObject("http://" + SERVICE_NAME, String.class);
        System.out.println(html);
        Assertions.assertNotNull(html);
    }
}
