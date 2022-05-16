package moe.ahao.web.module;

import moe.ahao.domain.entity.Result;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.RandomHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {SimpleController.class,
    WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
})
public class SimpleControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testPath() throws Exception {
        Integer value = RandomHelper.getInt(100);

        mockMvc.perform(get("/simple/path-" + value))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(value.toString()));
    }

    @Test
    public void testGet() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        for (int i = 1; i <= 3; i++) {
            // TODO NPE
            mockMvc.perform(get("/simple/get" + i)
                .param("code", String.valueOf(result))
                .param("msg", msg))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONHelper.toString(Result.get(result, msg, null))));
        }
    }

    @Test
    public void testPost123() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        for (int i = 1; i <= 3; i++) {
            // TODO NPE
            mockMvc.perform(post("/simple/post" + i)
                .param("code", String.valueOf(result))
                .param("msg", msg))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONHelper.toString(Result.get(result, msg, null))));
        }
    }

    @Test
    public void testPost4() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        mockMvc.perform(post("/simple/post" + 4)
            .param("msg", msg)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONHelper.toString(Result.get(result, msg, null))))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(JSONHelper.toString(Result.get(result, msg + msg, null))));
    }

    @Test
    public void testMultipart123() throws Exception {
        int result = Result.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        for (int i = 1; i <= 3; i++) {
            mockMvc.perform(multipart("/simple/multipart" + i)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONHelper.toString(Result.get(result, msg, null))));
        }
    }

    @Test
    public void testMultipart4() throws Exception {
        int result = Result.SUCCESS;
        String msg = "Hello world";
        String param = "?msg=" + msg + "&result=" + result;
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/simple/multipart" + 4 + param)
            .file(file))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(JSONHelper.toString(Result.get(result, msg + msg, null))));
    }

    @Test
    public void testMultipart56() throws Exception {
        int result = Result.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile req = new MockMultipartFile("req", "", "application/json", JSONHelper.toString(Result.success(msg)).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        for (int i = 5; i <= 6; i++) {
            mockMvc.perform(multipart("/simple/multipart" + i)
                .file(req)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONHelper.toString(Result.get(result, msg + msg, null))));
        }
    }
}
