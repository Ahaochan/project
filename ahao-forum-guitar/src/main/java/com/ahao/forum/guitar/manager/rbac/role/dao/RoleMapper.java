package com.ahao.forum.guitar.manager.rbac.role.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 角色的DAO层
 */
@Repository
public interface RoleMapper {
    /**
     * 插入一条 角色数据
     * @param baseDO      封装id的实体类
     * @param name        角色名称
     * @param description 角色描述
     * @param weight      角色权值
     * @param enabled     分区状态
     * @param createTime  创建时间
     * @return 是否插入成功
     */
    boolean saveRole(@Param("baseDO") BaseDO baseDO,
                     @Param("name") String name, @Param("description") String description,
                     @Param("weight") int weight, @Param("enabled") int enabled,
                     @Param("createTime") Date createTime);

    /**
     * 根据 角色id 更新字段
     * @param roleId      角色id
     * @param name        角色名称
     * @param description 角色描述
     * @param weight      角色权值
     * @param enabled     分区状态
     * @param modifyTime  修改时间
     * @return 是否更新成功
     */
    boolean updateRole(@Param("roleId") long roleId,
                           @Param("name") String name, @Param("description") String description,
                           @Param("weight") int weight, @Param("enabled") int enabled,
                           @Param("modifyTime") Date modifyTime);

    /**
     * 关联 角色 和 权限
     * @param roleId 角色id
     * @param authIds 权限id
     * @return 是否关联成功
     */
    boolean relateRoleAuth(@Param("roleId") long roleId, @Param("authIds") long... authIds);

    /**
     * 删除 角色
     * @param roleIds 角色id数组
     * @return 返回删除记录条数
     */
    int deleteRole(@Param("roleIds") long... roleIds);

    /**
     * 获取该角色下所有的权限, 标记起来
     * @param roleId 角色id
     */
    List<IDataSet> getSelectedAuthsByRoleId(@Param("roleId") long roleId);

    /**
     * 获取所有角色
     * @param search 查找角色名
     */
    List<IDataSet> getRoles(@Param("search") String search);

    /**
     * 根据 角色id 获取 角色 的基本信息
     * @param roleId 角色id
     * @param fields admin_role表的字段
     */
    IDataSet getRoleById(@Param("roleId") long roleId, @Param("fields") String... fields);
}

