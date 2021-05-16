package moe.ahao.spring.boot.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 继承 {@link WebSecurityConfigurerAdapter} 以覆盖 {@link SpringBootWebSecurityConfiguration} 配置
 * 使用 {@link EnableWebSecurity} 修饰配置类, 以启用 {@link WebSecurityConfiguration} 的 springSecurityFilterChain
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public static final String URL_LOGIN = "/login";
    public static final String URL_DO_LOGIN = "/doLogin";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1. 登录界面
        http.formLogin(f -> f
            .loginPage(URL_LOGIN)          // 登录页面
            .loginProcessingUrl(URL_DO_LOGIN)     // 登录请求url
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .permitAll()
        )
            .logout(l-> l
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
            )
//            .rememberMe(r -> r.tokenRepository(null)
//                .tokenValiditySeconds(1000)
//                .userDetailsService(userDetailsService)
//            )
            .authorizeRequests(r -> r
                .antMatchers(URL_LOGIN, "/swagger**").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(s -> s.invalidSessionUrl(URL_LOGIN))
            .csrf(
                s -> s
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .disable() // TODO 禁用csrf
            );
    }
}
