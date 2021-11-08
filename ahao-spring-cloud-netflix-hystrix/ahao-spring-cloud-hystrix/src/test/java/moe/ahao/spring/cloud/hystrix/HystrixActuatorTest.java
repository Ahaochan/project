package moe.ahao.spring.cloud.hystrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

@EnableCircuitBreaker
public class HystrixActuatorTest {
    private MockMvc mockMvc;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void actuator() throws Exception {
        mockMvc.perform(get("/actuator"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links['hystrix.stream']").exists());
    }

    @Test
    public void hystrixStream() throws Exception {
        // TODO 404
        mockMvc.perform(get("http://localhost:8080/actuator/hystrix.stream"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
