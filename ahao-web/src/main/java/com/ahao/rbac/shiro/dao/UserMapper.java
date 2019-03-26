package com.ahao.rbac.shiro.dao;

import com.ahao.rbac.shiro.entity.ShiroUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<ShiroUser> {
    /**
     * 根据 principal 查询用户名或邮箱对应的用户
     * @param principal 用户名或邮箱
     * @return 匹配的用户记录
     */
    ShiroUser selectByUsernameOrEmail(@Param("principal") String principal);
}
