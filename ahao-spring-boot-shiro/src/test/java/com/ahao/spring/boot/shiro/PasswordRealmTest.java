package com.ahao.spring.boot.shiro;

import com.ahao.spring.boot.Starter;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test-shiro")
public class PasswordRealmTest {
    public static final String PASSWORD = "asd";

    @Autowired
    protected WebApplicationContext wac;
//    @Autowired
//    private DataSource dataSource;

//    @BeforeEach
//    public void initSQL() throws Exception{
//        // 1. 初始化数据库
//        Connection connection = dataSource.getConnection();
//        Statement st = connection.createStatement();
//        st.execute(String.format("runscript from '%s'", new DefaultResourceLoader().getResource("sql/shiro-user.sql").getURL().toString()));
//        st.close();
//
//        // 2. 解决 Mock 不能加载 Filter 的 bug
//        SecurityUtils.setSecurityManager(SpringContextHolder.getBean(SecurityManager.class));
//    }

    @Test
    public void password() {
        String expect = "edfcc4776392b62693e975996a1f3a63a6de3bee81f43ba840b5721c10a2e65822ddf43b8dc43cd3dd6108a6c66771ac3b582dd1bb27d2ecdde5df6853b77d36";
        String salt = "qwe";
        ByteSource credentialsSalt = new Sha512Hash(salt);
        String actual = (new Sha512Hash(PASSWORD, credentialsSalt, 1024)).toString();
        Assertions.assertEquals(expect, actual);
    }

//    @Test
//    public void loginSuccess() throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String responseString = mockMvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", "admin")
//                .param("password", "asd"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn()
//                .getResponse().getContentAsString();   //将相应的数据转换为字符串
//        JSONObject json = JSONObject.parseObject(responseString);
//        Assertions.assertEquals(AjaxDTO.SUCCESS, json.getIntValue("result"));
//    }
//
//    @Test(expected = IncorrectCredentialsException.class)
//    public void errorPassword() throws Exception {
//        UsernamePasswordToken passwordToken = new UsernamePasswordToken("admin", "error", false);
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(passwordToken);
//        } finally {
//            passwordToken.clear();
//        }
//    }
}
