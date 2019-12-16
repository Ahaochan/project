package moe.ahao.spring.boot;

import moe.ahao.spring.boot.dependency.TestService;
import moe.ahao.spring.boot.log.filter.TraceLogFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Starter.class)
public class SampleTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private TestService testService;

    @BeforeEach
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(new TraceLogFilter(), "/*")
            .build();
    }

    @Test
    public void http() throws Exception {
        String responseString = mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        Assertions.assertEquals("hello", responseString);
    }

    @Test
    public void async() throws Exception {
        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            testService.async(latch);
        }

        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
    }
}
