package com.ahao.forum.guitar.module.admin.auth.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface AuthService {

    List<IDataSet> getSelectedAuth(Long userId, String... fields);
}
