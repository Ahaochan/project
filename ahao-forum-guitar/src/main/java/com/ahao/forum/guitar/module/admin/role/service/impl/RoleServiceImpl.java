package com.ahao.forum.guitar.module.admin.role.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.admin.role.dao.RoleMapper;
import com.ahao.forum.guitar.module.admin.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleMapper roleMapper;
    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper){
        this.roleMapper = roleMapper;
    }

    @Override
    public List<IDataSet> getSelectedRole(Long userId, String... fields) {
        if(userId == null || userId <= 0){
            return Collections.emptyList();
        }
        List<IDataSet> roles = roleMapper.getSelectedByUserId(userId, fields);
        return roles;
    }
}
