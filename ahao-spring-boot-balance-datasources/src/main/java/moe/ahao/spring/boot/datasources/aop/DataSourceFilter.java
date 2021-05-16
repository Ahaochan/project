package moe.ahao.spring.boot.datasources.aop;

import moe.ahao.spring.boot.datasources.DataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据源切换过滤器, 拦截请求头的 X-db 参数
 * 使用方法, 将 {@link #buildFilterBean(String...)} 方法注册为 Bean 即可
 */
public class DataSourceFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(DataSourceFilter.class);
    public static final String HEADER_DB = "X-db";

    public static FilterRegistrationBean<DataSourceFilter> buildFilterBean(String... urlPatterns) {
        FilterRegistrationBean<DataSourceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DataSourceFilter());
        registrationBean.addUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String oldContext = DataSourceContextHolder.get();
        String newContext = request.getHeader(HEADER_DB);
        logger.trace("当前数据源 {} 切换到 {} 数据源", oldContext, newContext);
        DataSourceContextHolder.set(newContext);
        try {
            filterChain.doFilter(request, response);
        } finally {
            DataSourceContextHolder.set(oldContext);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String[] suffixWhitelist = {".css", ".js", ".png", ".jpg", ".woff2", ".map", ".ico", ".html"};
        for (String suffix : suffixWhitelist) {
            if (StringUtils.endsWithIgnoreCase(path, suffix)) {
                return true;
            }
        }

        String[] prefixWhitelist = {"/null/swagger-resources", "/swagger", "/v2/api-docs", "/webjars"};
        for (String prefix : prefixWhitelist) {
            if (StringUtils.startsWithIgnoreCase(path, prefix)) {
                return true;
            }
        }

        return false;
    }
}
