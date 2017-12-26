package com.ahao.invoice.base.service.impl;

import com.ahao.context.PageContext;
import com.ahao.entity.BaseDO;
import com.ahao.invoice.base.service.PageService;
import com.ahao.util.lang.math.NumberHelper;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/7/17.
 *
 * 分页的Service层接口默认实现类
 */
public abstract class PageServiceImpl<T extends BaseDO> extends DataServiceImpl<T> implements PageService<T> {

    @Override
    public final Collection<?> getByPage(Integer page) {
        int p = NumberHelper.unboxing(page)[0];
        int pageSize = PageContext.getPageSize();
        String sort = PageContext.getSort();
        String order = PageContext.getOrder();
        return getByPage(p * pageSize, pageSize, sort, order);
    }


    protected abstract Collection<?> getByPage(int start, int pageSize, String sort, String order);

    @Override
    protected abstract Mapper<T> dao();
}
