package com.ahao.forum.guitar.manager.forum.service;

import com.ahao.commons.entity.IDataSet;

import java.util.List;

public interface ForumService {

    long saveForum(Long forumId, String name, String description, Integer status, String iconUrl);

    boolean deleteForum(Long... forumIds);

    List<IDataSet> getCategories(Long forumId);

    IDataSet getForum(Long forumId);
    List<IDataSet> getForums(Long userId, String search);
}
