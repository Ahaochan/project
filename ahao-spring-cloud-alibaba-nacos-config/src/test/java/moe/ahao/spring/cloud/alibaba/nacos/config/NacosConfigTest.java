package moe.ahao.spring.cloud.alibaba.nacos.config;

import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
// TODO nacos控制台不知道账号密码了
public class NacosConfigTest {

    @Value("${ahao.version}")
    private String version;

    @BeforeAll
    public static void init() {
        String[] profile = {"", "-test"};
        for (int i = 0; i < profile.length; i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("dataId", "ahao-nacos-config" + profile[i] + ".yaml");
            map.add("group", "DEFAULT_GROUP");
            map.add("content", "ahao.version: " + i + ".0.0");
            map.add("type", "yaml");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            String result = new RestTemplate().postForObject("http://console.nacos.io/nacos/v1/cs/configs", request, String.class);
            Assertions.assertEquals("true", result);
        }
    }

    @Test
    public void initTest() {
        Assertions.assertEquals("1.0.0", version);
    }
}
