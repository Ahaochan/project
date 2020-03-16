package moe.ahao.spring.cloud.alibaba.oss;

import moe.ahao.spring.cloud.Starter;
import moe.ahao.spring.cloud.alibaba.oss.service.AlibabaOssService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class OssTest {
    @Autowired
    private AlibabaOssService alibabaOssService;

    @Test
    public void test() {
        String url = alibabaOssService.putObject("hello", "world".getBytes(StandardCharsets.UTF_8));
        System.out.println(url);
        Assertions.assertNotNull(url);
    }
}
