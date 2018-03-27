package com.ahao.forum.guitar.module.admin.user.service;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONObject;

public interface UserService {

    AjaxDTO modifyPassword(Long userId, String oldPassword, String newPassword);


    IDataSet getProfile(Long userId, String... fields);

    JSONObject getUsersWithPage(Integer page, String search);
}
