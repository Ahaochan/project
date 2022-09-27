package moe.ahao.spring.boot.powermock;

import moe.ahao.spring.boot.Starter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class, Junit4Test.TestConfig.class})
public class Junit4Test {
    @Autowired
    private StringService stringService;

    @Configuration(proxyBeanMethods = false)
    public static class TestConfig {
        @Bean
        public StringService stringService() {
            return new StringService();
        }
    }

    @Test
    public void test() {
        String str = "HELLO";
        Assert.assertEquals(str.toLowerCase(), stringService.toLowerCase(str));
    }
}
