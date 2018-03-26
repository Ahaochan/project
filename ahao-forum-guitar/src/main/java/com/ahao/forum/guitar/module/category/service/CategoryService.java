package com.ahao.forum.guitar.module.category.service;

import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONArray;

public interface CategoryService {

    IDataSet getCategoryById(Long categoryId, String... fields);

    JSONArray getCategoryAndSub();
}
