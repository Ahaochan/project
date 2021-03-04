package moe.ahao.spring.cloud.contract;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@AutoConfigureStubRunner(ids = {"com.ahao:ahao-spring-cloud-contract-provider:+:stubs:18080"}, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class HelloTest {

    @Test
    public void helloTest() {
        // 设置环境变量maven.repo.local=D:\JetBrains\mvnlib, 否者会去~\.m2\repository查找

        ParameterizedTypeReference<String> ptf =  new ParameterizedTypeReference<String>() {};
        ResponseEntity<String> response = new RestTemplate().exchange("http://127.0.0.1:18080/hello?name=ahao", HttpMethod.GET, null, ptf);

        Assertions.assertEquals("helloahao", response.getBody());

    }
}
