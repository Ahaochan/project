package moe.ahao.spring.boot.shiro;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.spring.boot.Starter;
import moe.ahao.util.spring.SpringContextHolder;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = Starter.class)
@ActiveProfiles("cache-memory")
public class PasswordRealmTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

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
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "admin1")
            .param("password", "pw1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(AjaxDTO.SUCCESS));
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
