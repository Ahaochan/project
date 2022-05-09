package moe.ahao.spring.cloud.eureka;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.time.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = EurekaConsumerApplication.class)
public class FeignTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DiscoveryClient discoveryClient;

    @BeforeEach
    void serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("eureka-provider");
        Assumptions.assumeTrue(list.size() > 0, "必须先启动Eureka和Provider, 按照Readme.md文档进行测试");
        for (ServiceInstance serviceInstance : list) {
            System.out.println(serviceInstance.getUri().toString());
        }
    }

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
            .content(JSONHelper.toString(param)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        Assertions.assertEquals(param, JSONHelper.parse(response, AjaxDTO.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/form-data1", "/form-data2", "/form-data3"})
    public void formData(String url) throws Exception {
        String msg = DateHelper.getNow("yyyy-MM-dd HH:mm:ss");
        AjaxDTO param = AjaxDTO.failure(msg, msg);
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", msg.getBytes(StandardCharsets.UTF_8));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response = mockMvc.perform(multipart(url)
            .file(file)
            .param("param", msg)
            .param("json", JSONHelper.toString(param)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        AjaxDTO result = JSONHelper.parse(response, AjaxDTO.class);
        Assertions.assertEquals(AjaxDTO.SUCCESS, result.getResult());

        List<String> data = (List<String>) result.getObj();
        Assertions.assertEquals(3, data.size());
        Assertions.assertEquals(msg, data.get(0));
        Assertions.assertEquals(param, JSONHelper.parse(data.get(1), AjaxDTO.class));
        Assertions.assertEquals(msg, data.get(2));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/download1.txt", "/download2.txt", "/download3.txt"})
    public void download(String url) throws Exception {
        String name = "hello";
        String msg = DateHelper.getNow("yyyy-MM-dd HH:mm:ss");

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response = mockMvc.perform(get(url)
            .param("name", name)
            .param("data", msg))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        AjaxDTO result = JSONHelper.parse(response, AjaxDTO.class);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AjaxDTO.SUCCESS, result.getResult());

        String data = (String) result.getObj();
        Assertions.assertEquals(name, result.getMsg());
        Assertions.assertEquals(msg, data);
    }
}
