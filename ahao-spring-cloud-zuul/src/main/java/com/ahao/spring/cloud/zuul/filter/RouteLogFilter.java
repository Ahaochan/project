package com.ahao.spring.cloud.zuul.filter;

import com.ahao.spring.cloud.zuul.enums.ZuulFilterTypeEnum;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RouteLogFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(RouteLogFilter.class);

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.PRE.getFilterType();
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.debug("路由{}请求{}, 转发到{}", request.getMethod(), request.getRequestURL(), request.getRemoteAddr());
        return null;
    }
}
