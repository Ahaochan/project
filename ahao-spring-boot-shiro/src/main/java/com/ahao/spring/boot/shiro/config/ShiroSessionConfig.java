package com.ahao.spring.boot.shiro.config;

import com.ahao.spring.boot.shiro.listener.ShiroSessionListener;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "shiro.enabled", havingValue = "true", matchIfMissing = true)
public class ShiroSessionConfig {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 初始化 {@link DefaultWebSessionManager}.
     * 1. 设置 Session 监听器
     * 2. 设置 缓存/Session 处理器
     * 3. 设置 参数
     * 4. 设置 登陆后的 Cookie
     * @return 必须返回 {@link SessionsSecurityManager} 类型, 否则 {@link org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration} 内会有 Bean 冲突.
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // 1. 设置 Session 监听器
        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        sessionManager.setSessionListeners(listeners);

        // 2. 设置 缓存/Session 处理器
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setSessionDAO(enterpriseCacheSessionDAO());

        // 3. 设置参数
        sessionManager.setGlobalSessionTimeout(DefaultWebSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(DefaultWebSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL);
        sessionManager.setSessionIdUrlRewritingEnabled(false); // 取消 url 后面的 JSESSIONID

        // 4. 设置 登陆后的 Session 的 Cookie
        sessionManager.setSessionIdCookieEnabled(true);
        Cookie cookie = new SimpleCookie("sessionIdCookie");
        cookie.setDomain(".ahao.moe");
        cookie.setPath("/");
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }

    @Bean
    public SessionDAO enterpriseCacheSessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setCacheManager(cacheManager);
        sessionDAO.setActiveSessionsCacheName(CachingSessionDAO.ACTIVE_SESSION_CACHE_NAME);
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }
}
