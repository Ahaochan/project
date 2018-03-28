package com.ahao.forum.guitar.module.category.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.category.dao.CategoryMapper;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);


    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<IDataSet> getCategoryGroupByUserId(Long userId, String search, String... fields) {
        if(userId == null || userId <= 0){
            logger.debug("用户id非法:"+userId);
            return Collections.emptyList();
        }
        List<IDataSet> list = categoryMapper.getCategoryGroupByUserId(userId, search, fields);
        return list;
    }

    @Override
    public IDataSet getCategoryById(Long categoryId, String... fields) {
        if(categoryId == null || categoryId <= 0){
            logger.debug("板块id非法:"+categoryId);
            return null;
        }
        IDataSet data = categoryMapper.getCategoryById(categoryId, fields);
        return data;
    }

    @Override
    public JSONArray getCategoryAndSub() {
        JSONArray result = new JSONArray();
        String[] fields = {"id", "name", "description"};
        List<IDataSet> groups = categoryMapper.getCategoryByParentId(-1L, fields);
        for (IDataSet group : groups) {
            List<IDataSet> sub = categoryMapper.getCategoryByParentId(group.getLong("id"), fields);
            group.put("sub", sub);
            result.add(group);
        }
        return result;
    }
}
