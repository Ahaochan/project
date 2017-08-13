package com.ahao.service.impl;

import com.ahao.entity.BaseDO;
import com.ahao.service.DataService;
import com.ahao.util.ArrayHelper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by Ahaochan on 2017/6/20.
 *
 * 访问持久化层的Service的默认实现类, 使用Mybatis作为持久化层, 使用通用mapper
 * 包含基本的增删查改
 */
public abstract class DataServiceImpl<T extends BaseDO> implements DataService<T> {
    @Override
    public int insert(T record) {
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
        return dao().insertSelective(record);
    }

    @Override
    public boolean deleteByKey(Object... key) {
        List<Object> list = ArrayHelper.toList(key);

        Example example = new Example(clazz());
        example.createCriteria().andIn("id", list);
        return dao().deleteByExample(example) > 0;
    }

    @Override
    public int getAllCount() {
        return dao().selectCount(null);
    }

    @Override
    public T selectByKey(Long key) {
        return key == null ? null : dao().selectByPrimaryKey(key);
    }

    @Override
    public boolean isExist(T record) {
        return dao().selectCount(record) > 0;
    }

    @Override
    public int update(T obj) {
        // TODO updateByExampleSelective不能更新enable对象
        obj.setModifyTime(new Date());

        Example example = new Example(obj.getClass());
        example.createCriteria().andEqualTo("id", obj.getId());
        return dao().updateByExample(obj, example);
    }

    protected abstract Mapper<T> dao();

    protected abstract Class<T> clazz();
}
