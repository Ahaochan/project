package com.ahao.forum.guitar.manager.forum.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumMapper {

    /**
     * 删除 板块
     * @param forumIds 板块id数组
     * @return 返回删除记录条数
     */
    int deleteForum(@Param("forumIds") long... forumIds);

    IDataSet getForumById(@Param("forumId") long forumId, @Param("fields") String... fields);

    List<IDataSet> getForums(@Param("fields") String... fields);

    List<IDataSet> getForumsTable(@Param("userId") long userId, @Param("search") String search);
}
