package moe.ahao.spring.boot.actuator;

import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringJUnitWebConfig(classes = Starter.class)
class ActuatorTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Value("${management.endpoints.web.base-path}")
    private String prefix;

    @Test
    void health() throws Exception {
        mockMvc.perform(get(prefix + "/health"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shutdown() throws Exception {
        mockMvc.perform(post(prefix + "/shutdown"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Shutting down, bye..."));
    }
}
