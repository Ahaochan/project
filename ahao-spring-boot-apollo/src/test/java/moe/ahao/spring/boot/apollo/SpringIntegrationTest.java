package moe.ahao.spring.boot.apollo;

import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.mockserver.EmbeddedApollo;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringIntegrationTest.TestConfiguration.class)
public class SpringIntegrationTest {

    private static final String dbNs = "dbNs";

    @ClassRule
    public static EmbeddedApollo apollo = new EmbeddedApollo();

    @Test
    @DirtiesContext
    public void testInject() {
        Assertions.assertEquals("value1", testBean.key1);
        Assertions.assertEquals("value2", testBean.key2);
    }

    @Test
    @DirtiesContext
    public void testChange() throws Exception {
        String key = "ahao_key";
        String value = "ahao_value";
        apollo.addOrModifyProperty(dbNs, key, value);
        ConfigChangeEvent addEvent = testBean.future.get(5000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(dbNs, addEvent.getNamespace());
        Assertions.assertEquals(PropertyChangeType.ADDED, addEvent.getChange(key).getChangeType());
        Assertions.assertEquals(value, addEvent.getChange(key).getNewValue());

        apollo.deleteProperty(dbNs, key);
        ConfigChangeEvent delEvent = testBean.future.get(5000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(dbNs, delEvent.getNamespace());
        Assertions.assertEquals(PropertyChangeType.DELETED, delEvent.getChange(key).getChangeType());
    }

    // @Test
    // @DirtiesContext
    // public void testConfigurationProperties() throws Exception {
    //     Assertions.assertEquals("root", dbProperties.username);
    //     Assertions.assertEquals("root", dbProperties.password);
    //
    //     String key = "spring.datasource.username";
    //     String value = "ahao";
    //     apollo.addOrModifyProperty(dbNs, key, value);
    //     ConfigChangeEvent updateEvent = testBean.future.get(5000, TimeUnit.MILLISECONDS);
    //     Assertions.assertEquals(dbNs, updateEvent.getNamespace());
    //     Assertions.assertEquals(PropertyChangeType.MODIFIED, updateEvent.getChange("spring.datasource.username").getChangeType());
    //     Assertions.assertEquals(value, updateEvent.getChange(key).getNewValue());
    //
    //     Assertions.assertEquals(value, dbProperties.username);
    // }

    @Autowired
    private TestBean testBean;

    @EnableApolloConfig
    @Configuration
    static class TestConfiguration {

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }

    private static class TestBean {

        @Value("${key1:default}")
        private String key1;
        @Value("${key2:default}")
        private String key2;

        private CompletableFuture<ConfigChangeEvent> future = new CompletableFuture<>();

        @ApolloConfigChangeListener(dbNs)
        private void onChange(ConfigChangeEvent changeEvent) {
            future.complete(changeEvent);
            future = new CompletableFuture<>();
        }
    }
}
