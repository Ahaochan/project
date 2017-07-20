package com.ahao.service.impl;

import com.ahao.context.PageContext;
import com.ahao.entity.BaseDO;
import com.ahao.entity.DropDownListDTO;
import com.ahao.service.PageService;
import com.ahao.util.PageUrlBuilder;
import com.ahao.util.UrlBuilder;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ahaochan on 2017/7/17.
 */
public abstract class PageServiceImpl<T extends BaseDO> extends DataServiceImpl<T> implements PageService<T> {
    @Override
    public final DropDownListDTO getPageSize(int page, UrlBuilder urlBuilder) {
        DropDownListDTO dto = new DropDownListDTO();
        dto.setDefaultItem(new DropDownListDTO.Item(PageContext.getPageSize() + "", ""));
        dto.setItems(Stream.of(15, 25, 50, 100)
                .map(i -> new DropDownListDTO.Item(i + "",
                        urlBuilder.restUrl(PageContext.PAGE, page)
                                .param(PageContext.PAGE_SIZE, i)
                                .build()))
                .collect(Collectors.toList()));
        return dto;
    }

    @Override
    public final Collection<T> getByPage(Integer page) {
        int p = page == null? 1 : page;
        int pageSize = PageContext.getPageSize();
        String sort = PageContext.getSort();
        String order = PageContext.getOrder();
        return getByPage((p-1)*pageSize, pageSize, sort, order);
    }


    protected abstract Collection<T> getByPage(int start, int pageSize, String sort, String order);

    @Override
    protected abstract Mapper<T> dao();

}
