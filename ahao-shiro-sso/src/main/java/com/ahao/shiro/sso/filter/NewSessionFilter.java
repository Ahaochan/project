package com.ahao.shiro.sso.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 保证登录前后Session不同的临时解决方案
 * 重写 {@link ##executeLogin} 方法, 将 subject.login(token); 替换为 {@link ##login(Subject, AuthenticationToken)}
 *
 * @see <a href="https://issues.apache.org/jira/browse/SHIRO-170">Force New Session ID on Authentication</a>
 */
public class NewSessionFilter extends FormAuthenticationFilter {

    @Override
    protected boolean executeLogin(final ServletRequest request, final ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }

        try {
            Subject subject = getSubject(request, response);

            // ======================= 扩展点 =======================
            login(subject, token);
            // ======================= 扩展点 =======================

            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * 保证登录前后Session不同 的核心代码
     * @param subject 登录主体
     * @param token   验证token
     * @throws Exception 抛出的所有异常, 包括 RuntimeException
     */
    private void login(Subject subject, AuthenticationToken token) throws Exception {
        // https://issues.apache.org/jira/browse/SHIRO-170

        // 1. 获取旧的session的相关属性
        Session oldSession = subject.getSession();
        Map<Object, Object> attributes = oldSession.getAttributeKeys().stream()
                .collect(Collectors.toMap(k -> k, oldSession::getAttribute));

        // 2. 销毁旧的session
        oldSession.stop();

        // 3. 登录创建新的session
        subject.login(token);

        // 4. 恢复旧session的相关属性
        Session newSession = subject.getSession();
        attributes.forEach(newSession::setAttribute);
    }
}
