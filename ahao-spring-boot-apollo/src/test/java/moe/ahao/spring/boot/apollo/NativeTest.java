package moe.ahao.spring.boot.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import moe.ahao.spring.boot.apollo.config.ApolloExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.concurrent.CompletableFuture;

public class NativeTest {
    @RegisterExtension
    public static ApolloExtension apollo = new ApolloExtension();

    @Test
    public void testGetProperty() {
        Config applicationConfig = ConfigService.getConfig(ConfigConsts.NAMESPACE_APPLICATION);
        // 1. 简单的获取 key
        Assertions.assertEquals("value1", applicationConfig.getProperty("key1", null));
        // 2. 获取 key 后转为 int
        Assertions.assertEquals(Integer.valueOf("value2".length()), applicationConfig.getProperty("key2", String::length, null));
    }

    @Test
    public void testUpdateProperties() throws Exception {
        // 1. 读取 db 命名空间
        String dbNs = "db";
        Config dbConfig = ConfigService.getConfig(dbNs);

        // 2. 获取 properties 中定义的值
        Assertions.assertEquals("root", dbConfig.getProperty("spring.datasource.username", null));
        Assertions.assertEquals("root", dbConfig.getProperty("spring.datasource.password", null));

        // 3. 动态监听新增的值
        String key = "spring.datasource.new";
        String value = "ahao";
        CompletableFuture<ConfigChangeEvent> future = new CompletableFuture<>();
        dbConfig.addChangeListener(future::complete);

        apollo.addOrModifyProperty(dbNs, key, value);
        Assertions.assertTrue(future.get().isChanged(key));
        Assertions.assertEquals(value, dbConfig.getProperty(key, null));
    }
}
