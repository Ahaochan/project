package com.ahao.forum.guitar.manager.rbac.role.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface RoleService {
    long saveRole(Long roleId, String name, String description, Integer weight, Integer enabled, Long... authIds);

    boolean deleteRole(Long... roleIds);

    List<IDataSet> getRoles(String search);

    IDataSet getRole(Long roleId);

    List<IDataSet> getSelectedAuths(Long roleId);

}
