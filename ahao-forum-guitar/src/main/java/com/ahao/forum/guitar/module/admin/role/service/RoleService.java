package com.ahao.forum.guitar.module.admin.role.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface RoleService {

    List<IDataSet> getSelectedRole(Long userId, String... fields);
}
