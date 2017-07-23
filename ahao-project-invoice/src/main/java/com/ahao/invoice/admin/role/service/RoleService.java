package com.ahao.invoice.admin.role.service;

import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.service.PageService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Avalon on 2017/6/3.
 *
 * 角色Service层接口
 */
@Service
public interface RoleService extends PageService<RoleDO> {


    /**
     * 获取所有角色, 根据角色id, 对用户拥有的角色集合标记为true
     *
     * @param roleIds 选择的角色id
     * @return key为角色信息, value为是否用户的角色
     */
    Map<RoleDO, Boolean> getSelectedRole(String... roleIds);

    /**
     * 获取所有角色, 根据用户id, 对用户拥有的角色集合标记为true
     * @param userId 用户id
     * @return key为角色信息, value为是否用户的角色
     */
    Map<RoleDO, Boolean> getSelectedRole(Long userId);

    /**
     * 修改用户角色表, 增加多对多关系, 用于用户详情页面
     * @param userId 用户id
     * @param roleIds 角色id
     */
    void addRelate(Long userId, String[] roleIds);
}
