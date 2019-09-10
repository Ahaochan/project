package com.ahao.spring.boot.shiro;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.spring.boot.Starter;
import com.ahao.util.spring.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("cache-memory")
public class PasswordRealmTest {

    @Autowired
    protected WebApplicationContext wac;

    @BeforeEach
    void init() throws Exception {
        // 1. 解决 Mock 不能加载 Filter 的 bug
        SecurityManager securityManager = SpringContextHolder.getBean(SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Test
    public void password() {
        ByteSource credentialsSalt = new Sha1Hash("salt1");
        String expect = "15b1b32890ffc9f86c0c82852d06face0baba1d429c2c77bafd61d1bf85a544db9321a917404524752c27c90ca381c2cefbc96596295346de4c7bac1d50817a8";
        String actual = (new Sha512Hash("asd", credentialsSalt, 1024)).toString();
        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void loginSuccess() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "admin1")
            .param("password", "pw1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        JSONObject json = JSONObject.parseObject(responseString);
        Assertions.assertEquals(AjaxDTO.SUCCESS, json.getIntValue("result"));
    }


    @Test
    void errorPassword() throws Exception {
        Assertions.assertThrows(AuthenticationException.class, () -> {
            UsernamePasswordToken passwordToken = new UsernamePasswordToken("admin1", "error", false);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(passwordToken);
            } finally {
                passwordToken.clear();
            }
        });
    }
}
