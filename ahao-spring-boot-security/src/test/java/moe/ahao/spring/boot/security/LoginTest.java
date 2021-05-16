package moe.ahao.spring.boot.security;

import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
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
public class LoginTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }


    @Test
    public void loginSuccess() throws Exception{
        String responseString = mockMvc.perform(post("/doLogin")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "admin")
            .param("password", "admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println(responseString);
    }

    @Test
    public void loginNoUsername() throws Exception {
        String responseString1 = mockMvc.perform(post("/doLogin")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "admin1")
            .param("password", "admin1"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println(responseString1);
    }

    @Test
    public void loginPasswordError() throws Exception {
        String responseString1 = mockMvc.perform(post("/doLogin")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "admin")
            .param("password", "123456"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println(responseString1);
    }

    @Test
    public void noAuthenticated() throws Exception{
        String responseString1 = mockMvc.perform(get("/api/hello"))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println(responseString1);

        String responseString2 = mockMvc.perform(get("/hello"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println(responseString2);
    }
}
