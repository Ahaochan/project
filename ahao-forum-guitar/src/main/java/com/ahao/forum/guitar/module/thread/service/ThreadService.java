package com.ahao.forum.guitar.module.thread.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ThreadService {

    IDataSet getThread(Long threadId);
    List<IDataSet> getPosts(Long threadId);
}
