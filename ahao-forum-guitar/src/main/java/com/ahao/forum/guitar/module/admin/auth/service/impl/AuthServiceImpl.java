package com.ahao.forum.guitar.module.admin.auth.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.admin.auth.dao.AuthMapper;
import com.ahao.forum.guitar.module.admin.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthMapper authMapper;
    @Autowired
    public AuthServiceImpl(AuthMapper authMapper){
        this.authMapper = authMapper;
    }

    @Override
    public List<IDataSet> getSelectedAuth(Long userId, String... fields) {
        if(userId == null || userId <= 0){
            return Collections.emptyList();
        }
        List<IDataSet> auths = authMapper.getSelectedByUserId(userId, fields);
        return auths;
    }
}
