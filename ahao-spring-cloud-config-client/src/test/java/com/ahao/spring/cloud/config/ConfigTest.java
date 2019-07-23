package com.ahao.spring.cloud.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ContextConfiguration(classes = Starter.class)
//@ActiveProfiles("test")
class ConfigTest {

    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext wac;
//
//    @BeforeEach
//    void init() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
//    }

    @Test
    @Disabled("按照Readme.md文档进行测试")
    void read() throws Exception {
        String actual = mockMvc.perform(get("/test"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();   //将相应的数据转换为字符串
        String expect = "Spring Cloud Samples";
        Assertions.assertEquals(expect, actual);
    }
}
