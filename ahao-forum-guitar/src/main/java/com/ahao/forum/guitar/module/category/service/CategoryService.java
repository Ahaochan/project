package com.ahao.forum.guitar.module.category.service;

import com.ahao.core.entity.IDataSet;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

public interface CategoryService {

    List<IDataSet> getCategories(Long userId, String search);

    IDataSet getCategory(Long categoryId);

    int deleteCategory(Long... categoryIds);

    JSONArray getCategoryAndSub();
}
