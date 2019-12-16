package moe.ahao.spring.boot;

import moe.ahao.spring.boot.dependency.TestConfig;
import moe.ahao.spring.boot.dependency.TestService;
import moe.ahao.spring.boot.log.Constants;
import moe.ahao.spring.boot.log.filter.TraceLogFilter;
import moe.ahao.spring.boot.util.IDGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {Starter.class, TestConfig.class})
public class SampleTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private TestService testService;
    @Autowired
    private Executor executor;

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
        MDC.put(Constants.TRACE_ID, IDGenerator.generateID("Ahao"));

        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            testService.async(latch);
        }

        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
    }

    @Test
    public void threadPool() throws Exception {
        Logger logger = LoggerFactory.getLogger(SampleTest.class);
        MDC.put(Constants.TRACE_ID, IDGenerator.generateID("Ahao"));

        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                logger.trace("threadPool trace");
                logger.debug("threadPool debug");
                logger.info("threadPool info");
                logger.warn("threadPool warn");
                logger.error("threadPool error");
                latch.countDown();
            });
        }

        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
        Assertions.fail("没有打印REQ"); // TODO MDCTaskDecorator 可以满足目前需求, 暂不处理
    }
}
