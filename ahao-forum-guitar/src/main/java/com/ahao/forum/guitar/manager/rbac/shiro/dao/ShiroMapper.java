package com.ahao.forum.guitar.manager.rbac.shiro.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShiroMapper {
    /**
     * 更新最后一次登录的信息
     * @param lastLoginTi0me 最后一次登录时间
     * @param lastLoginIp    最后一次登录IP
     * @param userId         用户id
     */
    boolean updateLastLoginMsg(@Param("lastLoginTime") Date lastLoginTi0me, @Param("lastLoginIp") String lastLoginIp,
                               @Param("userId") long userId);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     */
    IDataSet getUserByUsername(@Param("username") String username);

    /**
     * 获取当前用户的权限值
     * @param userId 用户id
     */
    int getWeight(@Param("userId") long userId);

    /**
     * 根据用户id获取用户所拥有的角色信息
     * @param userId 用户id
     */
    List<IDataSet> getRolesByUserId(@Param("userId") long userId);

    /**
     * 根据用户id获取用户所拥有的权限信息
     * @param userId 用户id
     */
    List<IDataSet> getAuthsByUserId(@Param("userId") long userId);
}
