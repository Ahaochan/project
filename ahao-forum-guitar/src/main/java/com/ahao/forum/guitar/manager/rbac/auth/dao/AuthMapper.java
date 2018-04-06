package com.ahao.forum.guitar.manager.rbac.auth.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthMapper {


    /**
     * 插入一条 权限数据
     * @param baseDO 封装id的实体类
     * @param name 分区名称
     * @param description 分区描述
     * @param enabled 分区状态
     * @return 是否插入成功
     */
    boolean saveAuth(@Param("baseDO") BaseDO baseDO, @Param("name") String name, @Param("description") String description, @Param("enabled") int enabled);

    /**
     * 根据 权限id 更新字段
     * @param authId 权限id
     * @param name 权限名称
     * @param description 权限描述
     * @param enabled 权限状态
     * @return 是否更新成功
     */
    boolean updateAuth(@Param("authId") long authId, @Param("name") String name, @Param("description") String description, @Param("enabled") int enabled);

    /**
     * 为所有最高管理员添加此权限
     * @param authId 权限id
     * @param rootWeight 最高管理员的权限值
     */
    boolean relateRootAuth(@Param("authId") long authId, @Param("rootWeight") int rootWeight);

    /**
     * 删除 权限
     * @param authIds 权限id数组
     * @return 返回删除记录条数
     */
    int deleteAuth(@Param("authIds") long... authIds);

    /**
     * 根据 权限id 获取 权限 的基本信息
     * @param authId 权限id
     * @param fields category表的字段
     */
    IDataSet getAuthById(@Param("authId") long authId, @Param("fields") String... fields);

    /**
     * 获取所有权限信息
     * @param search 查找权限名
     */
    List<IDataSet> getAuths(@Param("search") String search);
}
