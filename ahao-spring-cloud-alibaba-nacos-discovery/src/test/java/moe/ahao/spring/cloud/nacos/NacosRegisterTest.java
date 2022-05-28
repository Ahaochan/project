package moe.ahao.spring.cloud.nacos;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

public class NacosRegisterTest {
    @Test
    public void test() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.setProperty("namespace", "public");

        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance("ahao", "127.0.0.1", 8080, "DEFAULT");
        Thread.sleep(1000);
        List<Instance> list1 = naming.getAllInstances("ahao-nacos-discovery");
        System.out.println(list1);
        Assertions.assertEquals(1, list1.size());

        naming.deregisterInstance("ahao", "127.0.0.1", 8080, "DEFAULT");
        Thread.sleep(1000);
        List<Instance> list2 = naming.getAllInstances("ahao-nacos-discovery");
        System.out.println(list2);
        Assertions.assertEquals(0, list2.size());
    }
}
