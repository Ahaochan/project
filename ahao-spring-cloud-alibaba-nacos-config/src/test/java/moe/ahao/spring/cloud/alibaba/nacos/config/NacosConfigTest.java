package moe.ahao.spring.cloud.alibaba.nacos.config;

import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
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

            String result = new RestTemplate().postForObject("http://192.168.19.128:8848/nacos/v1/cs/configs", request, String.class);
            Assertions.assertEquals("true", result);
        }
    }

    @Test
    public void initTest() {
        Assertions.assertEquals("0.0.0", version);
    }
}
