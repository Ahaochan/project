package com.ahao.forum.guitar.manager.rbac.role.service.impl;

import com.ahao.forum.guitar.manager.rbac.role.dao.RoleMapper;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleMapper roleMapper;
    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper){
        this.roleMapper = roleMapper;
    }

}
