package com.ahao.invoice.admin.role.service;

import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.service.PageService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

/**
 * Created by Avalon on 2017/6/3.
 * <p>
 * 角色Service层接口
 */
@Service
public interface RoleService extends PageService<RoleDO> {

    /**
     * 检查角色名是否存在
     *
     * @param name 角色名
     * @return 存在为true
     */
    boolean existName(String name);

    /**
     * 获取所有角色id、name、enable, 根据用户id, 对用户拥有的角色集合标记为true
     * 所有角色: data.roles
     * 角色id: data.roles[i].id
     * 角色名称: data.roles[i].name
     * 角色是否可用: data.roles[i].enabled
     * 角色是否被用户选中: data.roles[i].selected
     *
     * @param userId 用户id
     * @return
     */
    JSONArray getSelectedRole(Long userId);

    /**
     * 修改用户角色表, 增加多对多关系, 用于用户详情页面
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    void addRelate(Long userId, Long[] roleIds);
}
