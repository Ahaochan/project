package moe.ahao.spring.boot.log.filter;

import moe.ahao.spring.boot.log.Constants;
import moe.ahao.spring.boot.util.IDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web 拦截器
 * 使用方法: 将 {@link #getFilterRegistrationBean(String, int)} 返回的对象注册为 Bean 即可
 */
public class TraceLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 初始化 traceId
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = IDGenerator.generateID(Constants.TRACE_ID);
        }

        // 2. 切面注入 traceId
        MDC.put(Constants.TRACE_ID, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(Constants.TRACE_ID);
        }
    }

    /**
     * 交由 @Configuration 将 Filter 注入到 Spring 容器中
     *
     * @param urlPattern Filter 匹配的 url
     * @param order      Filter 加载顺序
     */
    public static FilterRegistrationBean getFilterRegistrationBean(String urlPattern, int order) {
        FilterRegistrationBean<TraceLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceLogFilter());
        registration.addUrlPatterns(urlPattern);
        registration.setName(TraceLogFilter.class.getSimpleName());
        registration.setOrder(order);
        return registration;
    }
}
