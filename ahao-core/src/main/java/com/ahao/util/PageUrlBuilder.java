package com.ahao.util;


import com.ahao.context.PageContext;
import com.ahao.util.UrlBuilder;

/**
 * Created by Avalon on 2017/6/23.
 *
 */
public class PageUrlBuilder extends UrlBuilder {

    public PageUrlBuilder(String baseUrl) {
        super(baseUrl);
        init();

    }

    public PageUrlBuilder(UrlBuilder urlBuilder) {
        super(urlBuilder);
        init();
    }

    public void init(){
        param(PageContext.PAGE_SIZE, PageContext.getPageSize());
        param(PageContext.ORDER, PageContext.getOrder());
        param(PageContext.SORT, PageContext.getSort());
    }
}
