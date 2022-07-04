package moe.ahao.spring.cloud.alibaba.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class NacosNativeTest {
    public static final String SERVER_ADDR = "192.168.19.128:8848";
    public static final String NAMESPACE_ID = ""; // 传命名空间的ID, 而不是名称, http://192.168.19.128:8848/nacos/#/namespace
    public static final String GROUP = "DEFAULT_GROUP";
    public static final String DATA_ID = "ahao-nacos-native";

    @Test
    void test() throws Exception {
        String dataId = DATA_ID;
        String group = GROUP;
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
        properties.put(PropertyKeyConst.NAMESPACE, NAMESPACE_ID);
        ConfigService configService = NacosFactory.createConfigService(properties);

        try {
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("读取配置:" + content);
            Assertions.assertNull(content);

            configService.addListener(dataId, group, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("配置发生变更:" + configInfo);
                }
            });

            String data = "content";
            boolean isPublishOk = configService.publishConfig(dataId, group, data);
            System.out.println("发布配置:" + isPublishOk);
            Assertions.assertTrue(isPublishOk);

            content = configService.getConfig(dataId, group, 5000);
            System.out.println("读取配置:" + content);
            Assertions.assertEquals(data, content);

            boolean isRemoveOk = configService.removeConfig(dataId, group);
            System.out.println("移除配置:" + isRemoveOk);
            Assertions.assertTrue(isRemoveOk);

            content = configService.getConfig(dataId, group, 5000);
            System.out.println("读取配置:" + content);
            Assertions.assertNull(content);
        } catch (Exception e) {
            e.printStackTrace();
            configService.shutDown();
            Assertions.fail();
        }
    }

    @Test
    void printData() throws Exception {
        String dataId = DATA_ID;
        String group = GROUP;
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
        properties.put(PropertyKeyConst.NAMESPACE, NAMESPACE_ID);
        ConfigService configService = NacosFactory.createConfigService(properties);

        try {
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("读取配置:" + content);
            Assertions.assertNotNull(content);
        } catch (Exception e) {
            e.printStackTrace();
            configService.shutDown();
            Assertions.fail();
        }
    }
}
