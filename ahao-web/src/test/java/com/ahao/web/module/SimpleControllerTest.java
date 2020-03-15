package com.ahao.web.module;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.util.commons.io.JSONHelper;
import com.ahao.util.commons.lang.RandomHelper;
import com.ahao.web.AhaoApplication;
import org.junit.jupiter.api.Assertions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = AhaoApplication.class)
public class SimpleControllerTest {
    @Autowired
    protected WebApplicationContext wac;

    @Test
    public void testPath() throws Exception {
        Integer value = RandomHelper.getInt(100);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(get("/simple/path-" + value))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        Assertions.assertEquals(value, Integer.valueOf(responseString));
    }

    @Test
    public void testGet() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        for (int i = 1; i <= 3; i++) {
            String responseString = mockMvc.perform(get("/simple/get" + i)
                .param("result", String.valueOf(result))
                .param("msg", msg))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();   //将相应的数据转换为字符串

            AjaxDTO expect = AjaxDTO.get(result, msg, null);
            AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
            Assertions.assertEquals(expect, actual);
        }
    }

    @Test
    public void testPost123() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        for (int i = 1; i <= 3; i++) {
            String responseString = mockMvc.perform(post("/simple/post" + i)
                .param("result", String.valueOf(result))
                .param("msg", msg))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();   //将相应的数据转换为字符串

            AjaxDTO expect = AjaxDTO.get(result, msg, null);
            AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
            Assertions.assertEquals(expect, actual);
        }
    }

    @Test
    public void testPost4() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(post("/simple/post" + 4)
            .param("msg", msg)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONHelper.toString(AjaxDTO.get(result, msg, null))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串

        AjaxDTO expect = AjaxDTO.get(result, msg + msg, null);
        AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
        Assertions.assertEquals(expect, actual);
    }
}
