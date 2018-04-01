package com.ahao.forum.guitar.module.forum.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ForumService {

    List<IDataSet> getForums(String... fields);
    List<IDataSet> getForums(Integer parentId);
}
