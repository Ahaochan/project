package com.ahao.forum.guitar.manager.forum.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("forumBackMapper")
public interface ForumMapper {
    /**
     * 插入一条 板块数据
     *
     * @param baseDO      封装id的实体类
     * @param name        板块名称
     * @param description 板块描述
     * @param status      板块状态
     * @return 是否插入成功
     */
    boolean saveForum(@Param("baseDO") BaseDO baseDO,
                      @Param("name") String name, @Param("description") String description,
                      @Param("status") int status, @Param("iconUrl") String iconUrl);

    /**
     * 根据 板块id 更新字段
     *
     * @param forumId     板块id
     * @param name        板块名称
     * @param description 板块描述
     * @param status      板块状态
     * @return 是否更新成功
     */
    boolean updateForum(@Param("forumId") long forumId,
                        @Param("name") String name, @Param("description") String description,
                        @Param("status") int status, @Param("iconUrl") String iconUrl);

    /**
     * 删除 板块
     *
     * @param forumIds 板块id数组
     * @return 返回删除记录条数
     */
    int deleteForum(@Param("forumIds") long... forumIds);

    IDataSet getForumById(@Param("forumId") long forumId, @Param("fields") String... fields);

    List<IDataSet> getForums(@Param("search") String search);

    List<IDataSet> getForumsBySuperModerator(@Param("userId") long userId, @Param("search") String search);

    List<IDataSet> getForumsByModerator(@Param("userId") long userId, @Param("search") String search);
}
