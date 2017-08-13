package com.ahao.invoice.admin.auth.service;

import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.service.PageService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

/**
 * Created by Avalon on 2017/6/7.
 * <p>
 * 权限Service层接口
 */
@Service
public interface AuthService extends PageService<AuthDO> {

    /**
     * 检查权限名是否存在
     *
     * @param name 权限名
     * @return 存在为true
     */
    boolean existName(String name);

    /**
     * 获取所有权限id、name、enable, 根据用户id, 对用户拥有的角色集合标记为true
     * 所有角色: data.auths
     * 角色id: data.auths[i].id
     * 角色名称: data.auths[i].name
     * 角色是否可用: data.auths[i].enabled
     * 角色是否被用户选中: data.auths[i].selected
     *
     * @param roleId 角色id
     * @return
     */
    JSONArray getSelectedAuth(Long roleId);

    /**
     * 修改角色权限表, 增加多对多关系, 用于角色详情页面
     *
     * @param roleId  角色id
     * @param authIds 权限id
     */
    void addRelate(Long roleId, Long[] authIds);
}
