package com.ahao.forum.guitar.module.forum.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("forumForeMapper")
public interface ForumMapper {

    IDataSet getForumById(@Param("forumId") long forumId);
    List<IDataSet> getThreadByForumId(@Param("forumId") long forumId, @Param("search") String search);
}
