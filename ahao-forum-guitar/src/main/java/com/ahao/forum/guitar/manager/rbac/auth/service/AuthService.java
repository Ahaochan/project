package com.ahao.forum.guitar.manager.rbac.auth.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface AuthService {
    long saveAuth(Long authId, String name, String description, Integer enabled);

    int deleteAuth(Long... authIds);

    IDataSet getAuth(Long authId);
    List<IDataSet> getAuths(String search);
}
