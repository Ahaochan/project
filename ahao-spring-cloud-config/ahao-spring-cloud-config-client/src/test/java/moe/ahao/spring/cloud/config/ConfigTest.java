package moe.ahao.spring.cloud.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {ConfigClientAutoConfiguration.class,
    ConfigurationPropertiesAutoConfiguration.class, ConfigurationPropertiesRebinderAutoConfiguration.class})
class ConfigTest {

    @Value("${info.description:default}")
    private String description;

    @Test
    void read() throws Exception {
        String expect = "Spring Cloud Samples";
        Assertions.assertEquals(expect, description);
    }
}
