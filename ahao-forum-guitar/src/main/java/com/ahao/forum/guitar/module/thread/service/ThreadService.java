package com.ahao.forum.guitar.module.thread.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ThreadService {

    long saveThread(Long threadId, String title, String content, Long userId, Long forumId);

    IDataSet getThread(Long threadId);


    List<IDataSet> getPosts(Long threadId);
}
