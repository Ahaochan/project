package moe.ahao.spring.boot.apollo;

import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import moe.ahao.spring.boot.apollo.config.ApolloExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {ApolloConfig.class, SpringIntegrationTest.TestConfiguration.class})
@ActiveProfiles("test")
@EnableConfigurationProperties
public class SpringIntegrationTest {
    public static final String dbNs = "db";

    @RegisterExtension
    public static ApolloExtension apollo = new ApolloExtension();

    @Test
    @DirtiesContext
    public void testInject() {
        Assertions.assertEquals("value1", testBean.key1);
        Assertions.assertEquals("value2", testBean.key2);

        Assertions.assertEquals("root", dbProperties.username);
        Assertions.assertEquals("root", dbProperties.password);
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

    @Test
    @DirtiesContext
    public void testConfigurationProperties() throws Exception {
        Assertions.assertEquals("root", dbProperties.username);
        Assertions.assertEquals("root", dbProperties.password);

        String key = "spring.datasource.username";
        String value = "ahao";
        apollo.addOrModifyProperty(dbNs, key, value);
        ConfigChangeEvent updateEvent1 = testBean.future.get(5000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(dbNs, updateEvent1.getNamespace());
        Assertions.assertEquals(PropertyChangeType.MODIFIED, updateEvent1.getChange("spring.datasource.username").getChangeType());
        Assertions.assertEquals(value, updateEvent1.getChange(key).getNewValue());
        // Assertions.assertEquals(value, dbProperties.username);

        apollo.addOrModifyProperty(dbNs, key, "root");
        ConfigChangeEvent updateEvent2 = testBean.future.get(5000, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(dbNs, updateEvent2.getNamespace());
        Assertions.assertEquals(PropertyChangeType.MODIFIED, updateEvent2.getChange("spring.datasource.username").getChangeType());
        Assertions.assertEquals("root", updateEvent2.getChange(key).getNewValue());
    }

    @Autowired
    private TestBean testBean;

    @Autowired
    private DBProperties dbProperties;

    @EnableApolloConfig
    @Configuration
    public static class TestConfiguration {

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }

        @Bean
        @ConfigurationProperties("spring.datasource")
        public DBProperties dbProperties() {
            return new DBProperties();
        }
    }

    public static class TestBean {
        @Value("${key1:default}")
        public String key1;
        @Value("${key2:default}")
        public String key2;

        public CompletableFuture<ConfigChangeEvent> future = new CompletableFuture<>();

        @ApolloConfigChangeListener(SpringIntegrationTest.dbNs)
        private void onChange(ConfigChangeEvent changeEvent) {
            future.complete(changeEvent);
            future = new CompletableFuture<>();
        }
    }

    public static class DBProperties {
        public String url;
        public String username;
        public String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "DBProperties{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
        }
    }
}
