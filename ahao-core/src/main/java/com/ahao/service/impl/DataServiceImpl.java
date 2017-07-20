package com.ahao.service.impl;

import com.ahao.entity.BaseDO;
import com.ahao.service.DataService;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by Ahaochan on 2017/6/20.
 *
 * 访问持久化层的Service的默认实现类, 使用Mybatis作为持久化层, 使用通用mapper
 * 包含基本的增删查改
 */
public abstract class DataServiceImpl<T extends BaseDO> implements DataService<T> {

    @Override
    public int insert(T record) {
        return dao().insert(record);
    }

    @Override
    public int deleteByKey(Object key) {
        return dao().deleteByPrimaryKey(key);
    }

    @Override
    public int getAllCount() {
        return dao().selectCount(null);
    }

    @Override
    public T selectByKey(Object key) {
        return key == null ? null : dao().selectByPrimaryKey(key);
    }

    @Override
    public int update(T obj) {
        Example example = new Example(obj.getClass());
        example.createCriteria().andEqualTo("id", obj.getId());
        return dao().updateByExample(obj, example);
    }

    protected abstract Mapper<T> dao();
}
