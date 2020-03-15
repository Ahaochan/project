package moe.ahao.spring.cloud.openfeign;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.util.commons.io.JSONHelper;
import com.ahao.util.commons.lang.RandomHelper;
import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
public class LocalhostFeignApiTest {
    @Autowired
    protected WebApplicationContext wac;

    @Test
    public void testPath() throws Exception {
        Integer value = RandomHelper.getInt(100);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(get("/simple/path-" + value))
                            .andExpect(status().isOk())

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
                .andDo(print())
                .andExpect(status().isOk())
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
                .andDo(print())
                .andExpect(status().isOk())

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
            .andDo(print())
                .andExpect(status().isOk())

            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串

        AjaxDTO expect = AjaxDTO.get(result, msg + msg, null);
        AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void testMultipart123() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        for (int i = 1; i <= 3; i++) {
            String responseString = mockMvc.perform(multipart("/simple/multipart" + i)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn()
                .getResponse().getContentAsString();   //将相应的数据转换为字符串

            AjaxDTO expect = AjaxDTO.get(result, msg, null);
            AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
            Assertions.assertEquals(expect, actual);
        }
    }

    @Test
    public void testMultipart4() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        String param = "?msg=" + msg + "&result=" + result;
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String responseString = mockMvc.perform(multipart("/simple/multipart" + 4 + param)
            .file(file))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串

        AjaxDTO expect = AjaxDTO.get(result, msg+msg, null);
        AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void testMultipart56() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile req = new MockMultipartFile("req", "", "application/json", JSONHelper.toString(AjaxDTO.success(msg)).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        for (int i = 5; i <= 6; i++) {
            String responseString = mockMvc.perform(multipart("/simple/multipart" + i)
                .file(req)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();   //将相应的数据转换为字符串

            AjaxDTO expect = AjaxDTO.get(result, msg + msg, null);
            AjaxDTO actual = JSONHelper.parse(responseString, AjaxDTO.class);
            Assertions.assertEquals(expect, actual);
        }
    }
}
