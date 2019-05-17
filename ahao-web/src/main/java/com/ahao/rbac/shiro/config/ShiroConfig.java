package com.ahao.rbac.shiro.config;

import com.ahao.rbac.shiro.listener.ShiroSessionListener;
import com.ahao.rbac.shiro.realm.PasswordRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro 配置类
 * @see org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration
 * @see org.apache.shiro.spring.boot.autoconfigure.ShiroBeanAutoConfiguration
 * @see org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration
 */
@Configuration
@ConditionalOnProperty(name = "shiro.enabled", havingValue = "true", matchIfMissing = true)
public class ShiroConfig {

    /**
     * 将 {@link org.apache.shiro.web.servlet.ShiroFilter} 注册到 Servlet 容器
     * 若没配置, 则 {@link org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration} 会自动注入.
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("staticSecurityManagerEnabled", "true");
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * 初始化 {@link org.apache.shiro.web.servlet.ShiroFilter}
     * 1. 设置全局 SecurityManager
     * 2. 设置过滤器链
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 1. 等价于 SecurityUtils.setSecurityManager(securityManager());
        bean.setSecurityManager(securityManager());

        // 2. 设置拦截器
        Map<String, String> chains = new LinkedHashMap<>();
        chains.put("/druid/**", "anon");    // druid数据源监控页面不拦截
        chains.put("/static/**", "anon");   // 静态资源不拦截
        chains.put("/logout", "logout");    // 配置登出接口
        chains.put("/login", "anon");
        chains.put("/403", "anon");
        chains.put("/**", "user");          // user 记住我
        bean.setFilterChainDefinitionMap(chains);

        // 3. 设置登陆相关的页面
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/");
        bean.setUnauthorizedUrl("/unauthorized");
        return bean;
    }

    /**
     * 初始化 {@link DefaultWebSecurityManager}.
     * 1. 设置 Realm
     * 2. 设置 缓存 处理器
     * 3. 设置 记住我 处理器, {@link CookieRememberMeManager}
     * 3. 设置 Session 处理器
     * @return 必须返回 {@link SessionsSecurityManager} 类型, 否则 {@link org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration} 内会有 Bean 冲突.
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        // TODO 微信小程序 session 会出bug
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        // 1. 设置Realm
        List<Realm> realms = new ArrayList<>();
//        realms.add(weChatRealm()); // TODO 微信登陆
        realms.add(passwordRealm());
        manager.setRealms(realms);

        // 2. 配置一堆 Manager
        manager.setCacheManager(cacheManager());
        manager.setRememberMeManager(rememberMeManager());
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    /**
     * 初始化 {@link DefaultWebSessionManager}.
     * 1. 设置 Session 监听器
     * 2. 设置 缓存/Session 处理器
     * 3. 设置 参数
     * 4. 设置 登陆后的 Cookie
     * @return 必须返回 {@link SessionsSecurityManager} 类型, 否则 {@link org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration} 内会有 Bean 冲突.
     */
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // 1. 设置 Session 监听器
        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        sessionManager.setSessionListeners(listeners);

        // 2. 设置 缓存/Session 处理器
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setSessionDAO(sessionDAO());

        // 3. 设置参数
        sessionManager.setGlobalSessionTimeout(DefaultWebSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(DefaultWebSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL);
        sessionManager.setSessionIdUrlRewritingEnabled(false); // 取消 url 后面的 JSESSIONID

        // 4. 设置 登陆后的 Session 的 Cookie
        sessionManager.setSessionIdCookieEnabled(true);
        Cookie cookie = new SimpleCookie("shiroCookie");
        cookie.setDomain(".ahao.moe");
        cookie.setPath("/");
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();

        // 1. 设置 Cookie
        Cookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain(".ahao.moe");
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        rememberMeManager.setCookie(cookie);

        // 2. cookie加密的密钥, 默认 AesCipherService 加密
        rememberMeManager.setEncryptionCipherKey(Base64.decode("YWhhby5tb2U="));
        rememberMeManager.setDecryptionCipherKey(Base64.decode("bW9lLmFoYW8="));
        return rememberMeManager;
    }

    @Bean
    public SessionDAO sessionDAO() {
        // TODO 改为 Redis 分布式 Session
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setCacheManager(cacheManager());
        enterpriseCacheSessionDAO.setActiveSessionsCacheName(CachingSessionDAO.ACTIVE_SESSION_CACHE_NAME);
//        enterpriseCacheSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    @Bean
    protected CacheManager cacheManager() {
        // TODO 改为 Redis 分布式 缓存
//        EhCacheManager cacheManager = new EhCacheManager();
//        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return new MemoryConstrainedCacheManager();
    }

    // ====================================== Realm 初始化, 不能从外部 @Autowire =====================================
    @Bean
    public AuthorizingRealm passwordRealm() {
        PasswordRealm realm = new PasswordRealm();
        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }
    /**
     * HashedCredentialsMatcher，这个类是为了对密码进行编码的，
     * 防止密码在数据库里明码保存，当然在登陆认证的时候，
     * 这个类也负责对form里输入的密码进行编码。
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA-512");
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }
    // ====================================== Realm 初始化, 不能从外部 @Autowire =====================================
}
