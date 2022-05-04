package moe.ahao.spring.boot.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;

public class IniRealmTest {
    @Test
    public void test() {
        // 1. 加载 ini 配置, 初始化 SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 2. 获取 Subject
        Subject subject = SecurityUtils.getSubject();
        // TODO PasswordRealmTest影响本次单元测试结果
        Assertions.assertFalse(subject.isAuthenticated());

        // 3. 断言 会话属性是否正常
        Session session = subject.getSession();
        Serializable sessionId = session.getId();
        session.setAttribute("ahaoKey", "ahaoValue");
        String value = (String) session.getAttribute("ahaoKey");
        Assertions.assertNotNull(sessionId);
        Assertions.assertEquals("ahaoValue", value);

        // 4. 使用未知的帐号登录
        UsernamePasswordToken unKnowToken = new UsernamePasswordToken("error", "error");
        try {
            subject.login(unKnowToken);
            Assertions.fail();
        } catch (UnknownAccountException ignored) {
        }
        Assertions.assertFalse(subject.isAuthenticated());
        Assertions.assertEquals(sessionId, subject.getSession().getId());

        // 5. 使用错误的密码登录
        UsernamePasswordToken errorToken = new UsernamePasswordToken("username", "error");
        try {
            subject.login(errorToken);
            Assertions.fail();
        } catch (IncorrectCredentialsException ignored) {
        }
        Assertions.assertFalse(subject.isAuthenticated());
        Assertions.assertEquals(sessionId, subject.getSession().getId());

        // 6. 使用正确的密码登录
        UsernamePasswordToken trueToken = new UsernamePasswordToken("username", "password");
        try {
            subject.login(trueToken);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertTrue(subject.isAuthenticated());
        Assertions.assertEquals(sessionId, subject.getSession().getId());

        // 7. 角色判断
        Assertions.assertTrue(subject.hasRole("role1"));
        Assertions.assertTrue(subject.hasRole("role2"));
        Assertions.assertFalse(subject.hasRole("role3"));
        Assertions.assertTrue(subject.hasAllRoles(Arrays.asList("role1", "role2")));
        Assertions.assertFalse(subject.hasAllRoles(Arrays.asList("role1", "role3")));

        // 8. 权限判断
        Assertions.assertTrue(subject.isPermitted("permission1"));
        Assertions.assertTrue(subject.isPermitted("permission2"));
        Assertions.assertTrue(subject.isPermitted("permission3"));
        Assertions.assertTrue(subject.isPermitted("permission4"));
        Assertions.assertFalse(subject.isPermitted("permission5"));
        Assertions.assertTrue(subject.isPermittedAll("permission1", "permission2"));
        Assertions.assertFalse(subject.isPermittedAll("permission1", "permission5"));

        // 9. 登出注销
        subject.logout();
        Assertions.assertFalse(subject.hasRole("role1"));
        Assertions.assertFalse(subject.isPermitted("permission1"));
        Assertions.assertFalse(subject.isAuthenticated());
        Assertions.assertNotEquals(sessionId, subject.getSession().getId());
    }
}
