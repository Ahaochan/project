package com.ahao.forum.guitar.module.admin.user.service;

import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface UserService {
    JSONObject getUsersWithPage(Integer page, String search);
}
