package moe.ahao.spring.boot.dubbo;

import moe.ahao.spring.boot.DubboConsumerApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = DubboConsumerApplication.class)
public class SampleTest {

    @DubboReference(version = ProviderService.LAST_VERSION, timeout = 3000, retries = 0)
    private ProviderService providerService;

    @BeforeAll
    public static void beforeAll() {
        Assumptions.assumeTrue(false, "参考 README.md 进行测试");
    }

    @Test
    public void test() {
        String msg = "hello world";
        String result = providerService.toUpperCase(msg);
        Assertions.assertEquals(msg.toUpperCase(), result);
    }
}
