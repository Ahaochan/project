package moe.ahao.spring.boot.powermock;

import moe.ahao.spring.boot.Starter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class, Junit4MockTest.TestConfig.class})
public class Junit4MockTest {
    @Autowired
    private StringService stringService;

    @Configuration(proxyBeanMethods = false)
    public static class TestConfig {
        @Bean
        public StringService stringService() {
            return PowerMockito.mock(StringService.class);
        }
    }

    @Test
    public void thenAnswer() {
        PowerMockito.when(stringService.toLowerCase(Mockito.anyString())).thenAnswer(m -> m.getArguments()[0].toString().toUpperCase());

        String str = "hello";
        Assert.assertEquals(str.toUpperCase(), stringService.toLowerCase(str));
    }

    @Test
    public void thenReturn() {
        String expect = "world";
        PowerMockito.when(stringService.toLowerCase(Mockito.anyString())).thenReturn(expect);

        String str = "HELLO";
        Assert.assertEquals(expect, stringService.toLowerCase(str));
    }

    @Test
    public void noMock() {
        String str = "HELLO";
        Assert.assertNull(stringService.toLowerCase(str));
    }
}
