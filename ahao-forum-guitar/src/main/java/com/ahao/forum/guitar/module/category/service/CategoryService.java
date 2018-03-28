package com.ahao.forum.guitar.module.category.service;

import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

public interface CategoryService {

    List<IDataSet> getCategoryGroupByUserId(Long userId, String search, String... fields);

    IDataSet getCategoryById(Long categoryId, String... fields);

    JSONArray getCategoryAndSub();
}
