package moe.ahao.spring.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
class ConfigTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    void read() throws Exception {
        String actual = mockMvc.perform(get("/foo-db.properties"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();   //将相应的数据转换为字符串
        String expect = "democonfigclient.message: hello spring io\n" +
            "eureka.client.serviceUrl.defaultZone: http://localhost:8761/eureka/\n" +
            "foo: from foo props\n" +
            "foo.db: mycooldb\n" +
            "info.description: Spring Cloud Samples\n" +
            "info.url: https://github.com/spring-cloud-samples";
        Assertions.assertEquals(expect, actual);

    }
}
