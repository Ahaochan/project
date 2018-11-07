package com.ahao.shiro.sso.aop;

import com.ahao.shiro.sso.config.ShiroProperties;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 保证登录前后Session不同的临时解决方案
 * @see <a href="https://issues.apache.org/jira/browse/SHIRO-170">Force New Session ID on Authentication</a>
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = ShiroProperties.PREFIX, name = "new-session-id", havingValue = "true")
public class NewSessionIdAOP {

    @Pointcut("execution(* com.ahao.shiro.sso.controller.LoginController.doLogin(..)))")
    public void cutNewSessionId() {
    }

    @Around(value = "cutNewSessionId()")
    public Object valid(ProceedingJoinPoint point) throws Throwable {
        // https://issues.apache.org/jira/browse/SHIRO-170
        // 1. 获取旧的session的相关属性
        Session oldSession = SecurityUtils.getSubject().getSession();
        Map<Object, Object> attributes = oldSession.getAttributeKeys().stream()
                .collect(Collectors.toMap(k -> k, oldSession::getAttribute));

        // 2. 销毁旧的session
        oldSession.stop();

        // 3. 登录创建新的session
        Object result = point.proceed();

        // 4. 恢复旧session的相关属性
        Session newSession = SecurityUtils.getSubject().getSession();
        attributes.forEach(newSession::setAttribute);
        return result;
    }

}
