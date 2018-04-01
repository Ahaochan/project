package com.ahao.forum.guitar.module.category.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.module.category.dao.CategoryMapper;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    public List<IDataSet> getCategories(Long userId, String search) {
        if(userId == null || userId <= 0){
            logger.debug("用户id非法:"+userId);
            return Collections.emptyList();
        }
        List<IDataSet> list = categoryMapper.getCategoriesByUserId(userId, search, "id", "name", "status");
        return list;
    }

    @Override
    public IDataSet getCategory(Long categoryId) {
        if(categoryId == null || categoryId <= 0){
            logger.debug("板块id非法:"+categoryId);
            return null;
        }
        IDataSet data = categoryMapper.getCategoryById(categoryId);
        return data;
    }

    @Override
    public int deleteCategory(Long... categoryIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long categoryId : categoryIds) {
            if(categoryId == null || categoryId <= 0){
                continue;
            }
            IDataSet data = categoryMapper.getCategoryById(categoryId, "id");
            if(data!=null){
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if(oneExist) {
            int deleteCount = categoryMapper.deleteCategory(NumberHelper.unboxing(categoryIds));
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除分区失败, 数据表中不存在id:"+ Arrays.toString(categoryIds) + "的记录");
        return 0;
    }

    @Override
    public JSONArray getCategoryAndSub() {
        JSONArray result = new JSONArray();
//        String[] fields = {"id", "name", "description"};
//        List<IDataSet> groups = categoryMapper.getCategoryByParentId(-1L, fields);
//        for (IDataSet group : groups) {
//            List<IDataSet> sub = categoryMapper.getCategoryByParentId(group.getLong("id"), fields);
//            group.put("sub", sub);
//            result.add(group);
//        }
        return result;
    }
}
