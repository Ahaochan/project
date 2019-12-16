package moe.ahao.spring.boot.log.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import moe.ahao.spring.boot.log.Constants;
import org.slf4j.MDC;

/**
 * Feign 拦截器, 从上游服务中获取 traceId, 放入 MDC 中
 * 使用方法: 注册为 Bean 即可
 */
public class TraceLogFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = MDC.get(Constants.TRACE_ID);
        requestTemplate.header(Constants.TRACE_ID, traceId);
    }
}
