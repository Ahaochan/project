package com.ahao.filter;


import com.ahao.context.PageContext;
import com.ahao.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class PageFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(PageFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (!StringHelper.endsWith(request.getRequestURI(),
                ".css", ".js", ".png", ".jpg", ".woff2", ".map", ".ico")) {
            logger.debug("请求路径: " + request.getRequestURL());
            initPageSize(req);
            initOrder(req);
            initSort(req);
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig cfg) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    /**
     * 获取并设置分页大小, 默认25
     *
     * @param request Request请求
     */
    private void initPageSize(ServletRequest request) {
        String pageSize = request.getParameter(PageContext.PAGE_SIZE);
        logger.debug("分页大小:" + pageSize);
        if (StringHelper.isNumeric(pageSize)) {
            PageContext.setPageSize(Integer.parseInt(pageSize));
        } else {
            PageContext.setPageSize(PageContext.DEFAULT_PAGE_SIZE);
        }
    }

    /**
     * 获取并设置分页倒序还是正序, 默认正序
     *
     * @param request Request请求
     */
    private void initOrder(ServletRequest request) {
        String order = request.getParameter(PageContext.ORDER);
        logger.debug("排序方式:" + order);
        if (StringHelper.isNotEmpty(order)) {
            PageContext.setOrder(order);
        } else {
            PageContext.setOrder(PageContext.DEFAULT_ORDER);
        }
    }

    /**
     * 获取并设置分页排序字段, 默认为id
     *
     * @param request Request请求
     */
    private void initSort(ServletRequest request) {
        String sort = request.getParameter(PageContext.SORT);
        logger.debug("排序字段:" + sort);
        if (StringHelper.isNotEmpty(sort)) {
            PageContext.setSort(sort);
        } else {
            PageContext.setSort(PageContext.DEFAULT_SORT);
        }
    }
}
