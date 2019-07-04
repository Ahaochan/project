package com.ahao.spring.boot.actuator;

import com.ahao.spring.boot.Starter;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
class ActuatorTest {

    @Autowired
    protected WebApplicationContext wac;

    @Value("${management.endpoints.web.base-path}")
    private String prefix;


    @Test
    void health() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String responseString = mockMvc.perform(get(prefix+"/health"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        System.out.println(responseString);

        JSONObject json = JSONObject.parseObject(responseString);
        Assertions.assertEquals("UP", json.getString("status"));
    }

    @Test
    void shutdown() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String responseString = mockMvc.perform(post(prefix+"/shutdown"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        System.out.println(responseString);

        JSONObject json = JSONObject.parseObject(responseString);
        Assertions.assertEquals("Shutting down, bye...", json.getString("message"));
    }

}
