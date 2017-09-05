package com.ahao.service;

import com.ahao.entity.BaseDO;

import java.util.Collection;

/**
 * Created by Avalon on 2017/6/
 *
 * 抽象的分页Service
 */
public interface PageService<T extends BaseDO> extends DataService<T> {

    Collection<?> getByPage(Integer page);
}
