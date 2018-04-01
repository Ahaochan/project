package com.ahao.forum.guitar.module.category.service.impl;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.module.category.dao.CategoryMapper;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public long saveCategory(Long categoryId, String name, String description, Integer status, Long... forumIds) {
        // 1. 数据初始化
        if (status == null || status < 0) {
            status = 1;
        }
        // 2. 如果 categoryId 不存在, 则插入数据, 并添加联系
        if (categoryId == null || categoryId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            categoryMapper.saveCategory(idDO, name, description, status);
            Long id = idDO.getId();
            logger.debug("测试:"+idDO+","+ Arrays.toString(forumIds));
            // 2.2. 插入失败则返回 false
            if (id == null || id < 0) {
                return -1;
            }
            // 2.3. 插入成功则添加联系
            else {
                categoryMapper.relateCategoryForum(id, NumberHelper.unboxing(forumIds));
                categoryId = id;
            }
        }
        // 3. 如果 categoryId 存在, 则更新数据, 并添加联系
        else {
            // 3.1. 更新数据
            boolean success = categoryMapper.updateCategory(categoryId, name, description, status);
            // 3.2. 更新成功则添加联系
            categoryMapper.relateCategoryForum(categoryId, NumberHelper.unboxing(forumIds));
        }

        // 4. 为所有最高管理员添加该分区的联系
        categoryMapper.relateRootCategory(categoryId);
        return categoryId;
    }

    @Override
    public List<IDataSet> getCategories(Long userId, String search) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return Collections.emptyList();
        }
        List<IDataSet> list = categoryMapper.getCategoriesByUserId(userId, search, "id", "name", "status");
        return list;
    }

    @Override
    public IDataSet getCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            logger.debug("板块id非法:" + categoryId);
            return null;
        }
        IDataSet data = categoryMapper.getCategoryById(categoryId);
        return data;
    }

    @Override
    public List<IDataSet> getForums() {
        return null;
    }

    @Override
    public List<IDataSet> getSelectedForums(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            logger.debug("板块id非法:" + categoryId);
            return null;
        }
        List<IDataSet> list = categoryMapper.getSelectedForumsByCategoryId(categoryId);
        return list;
    }

    @Override
    public int deleteCategory(Long... categoryIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long categoryId : categoryIds) {
            if (categoryId == null || categoryId <= 0) {
                continue;
            }
            IDataSet data = categoryMapper.getCategoryById(categoryId, "id");
            if (data != null) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = categoryMapper.deleteCategory(NumberHelper.unboxing(categoryIds));
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除分区失败, 数据表中不存在id:" + Arrays.toString(categoryIds) + "的记录");
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
