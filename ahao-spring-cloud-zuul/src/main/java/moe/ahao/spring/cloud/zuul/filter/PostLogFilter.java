package moe.ahao.spring.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Post 阶段的请求日志打印, 记录请求的结束时间和耗时
 * 结合{@link PreLogFilter}使用
 * @see PreLogFilter
 */
@Component
public class PostLogFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(PostLogFilter.class);

    public static final String KEY_START_TIME = "zuul-log-start-time";

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true; // 是否需要执行该过滤器
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        try {
            long startTime = (long) ctx.get(PreLogFilter.KEY_START_TIME);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.debug("路由{}请求{}, 转发到{}, 耗时{}毫秒", request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), duration);
        } catch (Exception e) {
            ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ctx.set("error.exception", e);
        }
        return null;
    }
}
