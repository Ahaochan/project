package com.ahao.forum.guitar.module.post.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostMapper {

    boolean savePost(@Param("baseDO") BaseDO baseDO, @Param("prePostId") long prePostId, @Param("content") String content,
                       @Param("userId") long userId, @Param("threadId") long threadId, @Param("createTime") Date createTime);

    boolean sendNotification(long sendUserId, long receiverUserId);

    boolean updatePost(@Param("postId") long postId, @Param("prePostId") long prePostId, @Param("content") String content,
                         @Param("userId") long userId, @Param("threadId") long threadId, @Param("modifyTime") Date modifyTime);

    int deletePost(@Param("postIds") long... postIds);

    IDataSet getThreadById(@Param("threadId") long threadId);
    IDataSet getThreadByPostId(@Param("postId") long postId);

    IDataSet getPost(@Param("postId") long postId);

}
