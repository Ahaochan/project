package com.ahao.spring.boot.shiro.config;

import com.ahao.spring.boot.shiro.realm.PasswordRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordRealm passwordRealm;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SessionManager sessionManager;

    /**
     * 将 {@link org.apache.shiro.web.servlet.ShiroFilter} 注册到 Servlet 容器
     * 若没配置, 则 {@link org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration} 会自动注入.
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistration = new FilterRegistrationBean<>();
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
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 1. 设置Realm
        List<Realm> realms = new ArrayList<>();
//        realms.add(weChatRealm()); // TODO 微信登陆
        realms.add(passwordRealm);
        securityManager.setRealms(realms);

        // 2. 配置一堆 Manager
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager);
        return securityManager;
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
}
