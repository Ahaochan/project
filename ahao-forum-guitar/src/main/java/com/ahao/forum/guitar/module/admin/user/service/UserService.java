package com.ahao.forum.guitar.module.admin.user.service;

import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONObject;

public interface UserService {
    IDataSet getProfile(Long userId, String... fields);


    JSONObject getUsersWithPage(Integer page, String search);
}
