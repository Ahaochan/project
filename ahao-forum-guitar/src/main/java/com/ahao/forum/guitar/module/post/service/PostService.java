package com.ahao.forum.guitar.module.post.service;

import com.ahao.commons.entity.IDataSet;

public interface PostService {

    long savePost(Long postId, Long prePostId, String content, Long userId, Long threadId);

    int deletePost(Long... postIds);

    IDataSet getThreadById(Long threadId);
    IDataSet getThreadByPostId(Long postId);
    IDataSet getPost(Long postId);

}
