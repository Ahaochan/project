package moe.ahao.spring.cloud.alibaba.oss;

import moe.ahao.spring.cloud.Starter;
import moe.ahao.spring.cloud.alibaba.oss.service.AlibabaOssService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
// TODO ${spring.cloud.alicloud.access-key} can't be empty.
public class OssTest {
    @Autowired
    private AlibabaOssService alibabaOssService;

    @Test
    public void test() {
        String key = "hello";

        String url1 = alibabaOssService.putObject(key, "world".getBytes(StandardCharsets.UTF_8));
        System.out.println(url1);
        Assertions.assertNotNull(url1);

        String url2 = alibabaOssService.getUrl(key, 10, TimeUnit.MINUTES);
        System.out.println(url2);
        Assertions.assertNotNull(url2);
    }
}
