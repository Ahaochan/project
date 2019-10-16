package com.ahao.spring.boot.eureka;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.util.commons.lang.BeanHelper;
import com.ahao.util.commons.lang.time.DateHelper;
import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = {EurekaConsumerApplication.class})
public class FeignTest {

    @Autowired
    private WebApplicationContext wac;

    @ParameterizedTest
    @ValueSource(strings = {"/param1", "/param2", "/param3"})
    public void param(String url) throws Exception {
        String msg = DateHelper.getNow("yyyy-MM-dd HH:mm:ss");
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String response = mockMvc.perform(get(url)
            .param("msg", msg))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        Assertions.assertEquals(msg, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/body1", "/body2", "/body3"})
    public void body(String url) throws Exception {
        String msg = DateHelper.getNow("yyyy-MM-dd HH:mm:ss");
        AjaxDTO param = AjaxDTO.failure(msg, msg);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String response = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(BeanHelper.obj2JsonString(param)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        Assertions.assertEquals(param, BeanHelper.json2Obj(response, AjaxDTO.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/form-data1", "/form-data2", "/form-data3"})
    public void formData(String url) throws Exception {
        String msg = DateHelper.getNow("yyyy-MM-dd HH:mm:ss");
        AjaxDTO param = AjaxDTO.failure(msg, msg);
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", msg.getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response = mockMvc.perform(multipart(url)
            .file(file)
            .param("param", msg)
            .param("json", BeanHelper.obj2JsonString(param)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        AjaxDTO result = BeanHelper.json2Obj(response, AjaxDTO.class);
        Assertions.assertEquals(AjaxDTO.SUCCESS, result.getResult());

        List<String> data = (List<String>) result.getObj();
        Assertions.assertEquals(3, data.size());
        Assertions.assertEquals(msg, data.get(0));
        Assertions.assertEquals(param, BeanHelper.json2Obj(data.get(1), AjaxDTO.class));
        Assertions.assertEquals(msg, data.get(2));

    }
}
