package com.ahao.rbac.shiro;

import com.ahao.commons.entity.AjaxDTO;
import com.ahao.spring.util.SpringContextHolder;
import com.ahao.web.AhaoApplication;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = AhaoApplication.class)
@ActiveProfiles("test-shiro")
public class PasswordRealmTest {
    public static final String PASSWORD = "asd";

    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    private DataSource dataSource;


    @Before
    public void initSQL() throws Exception{
        // 1. 初始化数据库
        Connection connection = dataSource.getConnection();
        Statement st = connection.createStatement();
        st.execute(String.format("runscript from '%s'", new DefaultResourceLoader().getResource("sql/shiro-user.sql").getURL().toString()));
        st.close();

        // 2. 解决 Mock 不能加载 Filter 的 bug
        SecurityUtils.setSecurityManager(SpringContextHolder.getBean(SecurityManager.class));
    }

    @Test
    public void password() {
        String expect = "edfcc4776392b62693e975996a1f3a63a6de3bee81f43ba840b5721c10a2e65822ddf43b8dc43cd3dd6108a6c66771ac3b582dd1bb27d2ecdde5df6853b77d36";
        String salt = "qwe";
        ByteSource credentialsSalt = new Sha512Hash(salt);
        String actual = (new Sha512Hash(PASSWORD, credentialsSalt, 1024)).toString();
        Assert.assertEquals(expect, actual);
    }

    @Test
    public void loginSuccess() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "admin")
                .param("password", "asd"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();   //将相应的数据转换为字符串
        JSONObject json = JSONObject.parseObject(responseString);
        Assert.assertEquals(AjaxDTO.SUCCESS, json.getIntValue("result"));
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void errorPassword() throws Exception {
        UsernamePasswordToken passwordToken = new UsernamePasswordToken("admin", "error", false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(passwordToken);
        } finally {
            passwordToken.clear();
        }
    }
}
