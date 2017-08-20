package com.ahao.service;


import com.ahao.entity.BaseDO;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/6/20.
 *
 * 访问持久化层的Service
 * 包含基本的增删查改
 */
public interface DataService<T extends BaseDO> {
    /**
     * 插入一条数据
     * @param obj 数据
     * @return 返回受影响的记录数
     */
    int insert(T obj);

    /**
     * 删除多条数据
     * @param key 删除数据的主键
     * @return 返回受影响的记录数
     */
    boolean deleteByKey(Collection<?> key);

    /**
     * 获得所有数据的数量
     * @return 所有数据的数量
     */
    int getAllCount();

    /**
     * 根据record中非null字段进行and查询
     *
     * @param record 查询的实体
     * @return 是否存在record
     */
    boolean isExist(T record);

    /**
     * 根据主键 查找一条数据
     * @param key 数据的主键
     * @return 返回查找的数据
     */
    T selectByKey(Long key);

    /**
     * 更新一条数据
     * @param obj 更新的数据
     * @return 返回受影响的记录数
     */
    int update(T obj);
}
