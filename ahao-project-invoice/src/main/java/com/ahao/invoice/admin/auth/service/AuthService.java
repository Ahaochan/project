package com.ahao.invoice.admin.auth.service;

import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.service.PageService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by Avalon on 2017/6/7.
 */
@Service
public interface AuthService extends PageService<AuthDO> {
    /**
     * 获取所有权限, 根据角色id, 对角色拥有的权限集合标记为true
     * @param roleId 角色id
     * @return key为权限信息, value为是否角色的权限
     */
    Map<AuthDO, Boolean> getSelectedAuth(Long roleId);

    /**
     * 修改角色权限表, 增加多对多关系, 用于角色详情页面
     * @param roleId 角色id
     * @param authIds 权限id
     */
    void addRelate(Long roleId, Long[] authIds);
}
