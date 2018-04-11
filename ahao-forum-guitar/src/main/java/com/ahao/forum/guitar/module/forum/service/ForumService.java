package com.ahao.forum.guitar.module.forum.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ForumService {
    IDataSet getForum(Long forumId);
    List<IDataSet> getThread(Long forumId, String search);
}
