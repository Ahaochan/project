package moe.ahao.spring.cloud.openfeign;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.spring.cloud.Starter;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.RandomHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = Starter.class)
public class LocalhostFeignApiTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testPath() throws Exception {
        int value = RandomHelper.getInt(100);
        mockMvc.perform(get("/path-" + value))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(Integer.toString(value)));
    }

    @Test
    public void testGet() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        for (int i = 2; i <= 3; i++) {
            mockMvc.perform(get("/get" + i)
                .param("result", String.valueOf(result))
                .param("msg", msg))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(result))
                .andExpect(jsonPath("$.msg").value(msg))
                .andExpect(jsonPath("$.obj").isEmpty());
        }
    }

    @Test
    public void testPost23() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        for (int i = 2; i <= 3; i++) {
            mockMvc.perform(post("/post" + i)
                .param("result", String.valueOf(result))
                .param("msg", msg))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(result))
                .andExpect(jsonPath("$.msg").value(msg))
                .andExpect(jsonPath("$.obj").isEmpty());
        }
    }

    @Test
    public void testPost4() throws Exception {
        int result = RandomHelper.getInt(100);
        String msg = "Hello world";

        mockMvc.perform(post("/post" + 4)
            .param("msg", msg)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONHelper.toString(AjaxDTO.get(result, msg, null))))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(result))
            .andExpect(jsonPath("$.msg").value(msg + msg))
            .andExpect(jsonPath("$.obj").isEmpty());
    }

    @Test
    public void testMultipart123() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        for (int i = 1; i <= 3; i++) {
            if (i == 2) {
                continue; // TODO multipart2 不能使用
            }
            mockMvc.perform(multipart("/multipart" + i)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(result))
                .andExpect(jsonPath("$.msg").value(msg))
                .andExpect(jsonPath("$.obj").isEmpty());
        }
    }

    @Test
    public void testMultipart4() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        String param = "?msg=" + msg + "&result=" + result;
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/multipart" + 4 + param)
            .file(file))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(result))
            .andExpect(jsonPath("$.msg").value(msg + msg))
            .andExpect(jsonPath("$.obj").isEmpty());
    }

    @Test
    public void testMultipart56() throws Exception {
        int result = AjaxDTO.SUCCESS;
        String msg = "Hello world";
        MockMultipartFile req = new MockMultipartFile("req", "", "application/json", JSONHelper.toString(AjaxDTO.success(msg)).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        for (int i = 5; i <= 6; i++) {
            mockMvc.perform(multipart("/multipart" + i)
                .file(req)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(result))
                .andExpect(jsonPath("$.msg").value(msg + msg))
                .andExpect(jsonPath("$.obj").isEmpty());
        }
    }

    @Test
    public void fallback() throws Exception {
        mockMvc.perform(get("/fallback").contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.result").value(AjaxDTO.failure().getResult()))
            .andExpect(jsonPath("$.msg").value(AjaxDTO.failure().getMsg()))
            .andExpect(jsonPath("$.obj").isEmpty());
    }
}
