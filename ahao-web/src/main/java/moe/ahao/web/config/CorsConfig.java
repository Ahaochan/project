package moe.ahao.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration(proxyBeanMethods = false)
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://127.0.0.1:8080");
        config.setAllowCredentials(true); // 是否发送 Cookie 信息
        config.addAllowedMethod("*"); // 允许请求的方式
        config.addAllowedHeader("*"); // 允许请求的请求头

        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        CorsFilter filter = new CorsFilter(corsSource);
        return filter;
    }
}
