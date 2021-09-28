package moe.ahao.spring.cloud.ribbon;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.client.http.RestClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RibbonNativeTest {
    @Test
    public void test() {
        String serviceName = RibbonTest.SERVICE_NAME;
        String[] listOfServers = {"http://www.baidu.com:80/", "http://cn.bing.com:80/"};

        ConfigurationManager.getConfigInstance().setProperty(serviceName + ".ribbon.listOfServers", StringUtils.join(listOfServers, ','));

        RestClient client = (RestClient) ClientFactory.getNamedClient(serviceName);
        HttpRequest request = HttpRequest.newBuilder()
            .uri("/")
            .build();

        for (int i = listOfServers.length - 1; i >= 0; i--) {
            try (HttpResponse response = client.executeWithLoadBalancer(request);) {
                Assertions.assertEquals(200, response.getStatus());
                Assertions.assertTrue(response.isSuccess());
                String uri = response.getRequestedURI().toString();
                Assertions.assertEquals(listOfServers[i], uri);

                String entity = response.getEntity(String.class);
                System.out.println(uri + "======>" + entity);
                Assertions.assertNotNull(entity);
            } catch (Exception e) {
                Assertions.fail(e);
            }
        }
    }

    @DisplayName("轮询策略")
    @Test
    public void roundRobinRule() {
        BaseLoadBalancer balancer = new BaseLoadBalancer();

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            servers.add(new Server("server" + i, 80 + i));
        }
        balancer.setServersList(servers);

        for (int i = 1; i < 10; i++) {
            Server server = balancer.chooseServer(null);
            System.out.println(server.getHostPort());
            Assertions.assertEquals("server" + i, server.getHost());
            Assertions.assertEquals(80 + i, server.getPort());
        }

        balancer.setPing(new PingUrl());
        balancer.setPingInterval(1);

        Server server = balancer.chooseServer(null);
        System.out.println(server.getHostPort());
        Assertions.assertEquals("server" + 0, server.getHost());
        Assertions.assertEquals(80 + 0, server.getPort());
    }
}
