package com.ahao.forum.guitar.module.category.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.category.dao.CategoryMapper;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public JSONArray getCategoryAndSub() {
        JSONArray result = new JSONArray();
        String[] fields = {"id", "name", "description"};
        List<IDataSet> groups = categoryMapper.getCategory(-1L, fields);
        for (IDataSet group : groups) {
            List<IDataSet> sub = categoryMapper.getCategory(group.getLong("id"), fields);
            group.put("sub", sub);
            result.add(group);
        }
        return result;
    }
}
