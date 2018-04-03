package com.ahao.forum.guitar.manager.forum.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ForumService {

    long saveForum(Long forumId, Long categoryId, String name, String description, Integer status, String iconUrl);

    int deleteForum(Long... forumIds);

    List<IDataSet> getCategories();

    List<IDataSet> getForums(String... fields);
    List<IDataSet> getForumsTable(Long userId, String search);


}
