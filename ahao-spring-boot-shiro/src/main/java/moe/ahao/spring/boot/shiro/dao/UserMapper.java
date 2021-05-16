package moe.ahao.spring.boot.shiro.dao;

import moe.ahao.spring.boot.shiro.entity.ShiroUser;

public interface UserMapper {
    /**
     * 根据 principal 查询用户名或邮箱对应的用户
     * @param principal 用户名或邮箱
     * @return 匹配的用户记录
     */
    ShiroUser selectByUsernameOrEmail(String principal);
}
