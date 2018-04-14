package com.ahao.forum.guitar.module.thread.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ThreadMapper {
    boolean saveThread(@Param("baseDO") BaseDO baseDO, @Param("title") String title, @Param("content") String content,
                       @Param("userId") long userId, @Param("forumId") long forumId, @Param("createTime") Date createTime);

    boolean updateThread(@Param("threadId") long threadId, @Param("title") String title, @Param("content") String content,
                         @Param("userId") long userId, @Param("forumId") long forumId, @Param("modifyTime") Date modifyTime);

    int deleteThread(@Param("threadIds") long... threadIds);

    IDataSet getThreadById(@Param("threadId") long threadId);

    List<IDataSet> getPosts(@Param("threadId") long threadId);

    boolean isModerator(@Param("userId") long userId, @Param("threadId") long threadId);
    boolean isSuperModerator(@Param("userId") long userId, @Param("threadId") long threadId);

}
