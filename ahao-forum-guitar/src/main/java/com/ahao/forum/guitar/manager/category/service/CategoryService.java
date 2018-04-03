package com.ahao.forum.guitar.manager.category.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface CategoryService {

    long saveCategory(Long categoryId, String name, String description, Integer status, Long... forumIds);

    int deleteCategory(Long... categoryIds);

    List<IDataSet> getCategories(Long userId, String search);

    IDataSet getCategory(Long categoryId);

    List<IDataSet> getSelectedForums(Long categoryId);

}
