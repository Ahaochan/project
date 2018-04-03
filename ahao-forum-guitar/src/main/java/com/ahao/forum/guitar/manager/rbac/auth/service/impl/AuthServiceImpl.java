package com.ahao.forum.guitar.manager.rbac.auth.service.impl;

import com.ahao.forum.guitar.manager.rbac.auth.dao.AuthMapper;
import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthMapper authMapper;
    @Autowired
    public AuthServiceImpl(AuthMapper authMapper){
        this.authMapper = authMapper;
    }
}
