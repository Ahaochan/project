package moe.ahao.spring.boot.log.filter;

import moe.ahao.spring.boot.log.Constants;
import moe.ahao.spring.boot.util.IDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用方法: 编写 WebConfig 实现 WebMvcConfigurer 接口的 addInterceptors 方法, 将拦截器注入即可
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addInterceptors(InterceptorRegistry)
 */
public class TraceLogSpringInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 初始化 traceId
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = IDGenerator.generateID(Constants.TRACE_ID);
        }

        // 2. 切面注入 traceId
        MDC.put(Constants.TRACE_ID, traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(Constants.TRACE_ID);
    }
}
