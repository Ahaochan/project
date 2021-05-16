package moe.ahao.spring.boot.validator;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.validator.dependency.NestedObj;
import moe.ahao.spring.boot.validator.dependency.User;
import moe.ahao.util.commons.io.JSONHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
public class ValidatorTest {

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void testPath() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response1 = mockMvc.perform(get("/path-1-1").accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response1:" + response1);

        String response2 = mockMvc.perform(get("/path-6-6").accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("obj.id").value("超过 id 的范围了"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.age").value("超过 age 的范围了"))
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response2:" + response2);
    }

    @Test
    public void testGet1() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response1 = mockMvc.perform(get("/get1").accept(MediaType.APPLICATION_JSON_UTF8)
            .param("age", "10000")
            .param("email", "12345"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("obj.phone").value("手机号码不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.name").value("名称不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.email").value("邮箱格式错误"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.age").value("年龄不大于100"))
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response1:" + response1);
    }

    @Test
    public void testGet2() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response1 = mockMvc.perform(get("/get2").accept(MediaType.APPLICATION_JSON_UTF8)
            .param("id", "1")
            .param("age", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response1:" + response1);

        String response2 = mockMvc.perform(get("/get2").accept(MediaType.APPLICATION_JSON_UTF8)
            .param("id", "6")
            .param("age", "6"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("obj.id").value("超过 id 的范围了"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.age").value("超过 age 的范围了"))
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response2:" + response2);
    }

    @Test
    public void testPost1() throws Exception {
        User param = new User();
        param.setAge(10000);
        param.setEmail("12345");

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response1 = mockMvc.perform(post("/post1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONHelper.toString(param)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("obj.phone").value("手机号码不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.name").value("名称不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.email").value("邮箱格式错误"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.age").value("年龄不大于100"))
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response1:" + response1);
    }

    @Test
    public void testPost2() throws Exception {
        User param = new User();
        param.setAge(10000);
        param.setEmail("12345");
        NestedObj obj = new NestedObj();
        obj.setId(100);
        obj.setUser(param);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String response1 = mockMvc.perform(post("/post2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSONHelper.toString(obj)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("obj.id").value("id不大于10"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.['user.phone']").value("手机号码不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.['user.name']").value("名称不能为空"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.['user.email']").value("邮箱格式错误"))
            .andExpect(MockMvcResultMatchers.jsonPath("obj.['user.age']").value("年龄不大于100"))
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("response1:" + response1);
    }
}
