package com.ahao.forum.guitar.module.thread.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ThreadMapper {

    /**
     * 插入一条 主题数据
     *
     * @param baseDO     封装id的实体类
     * @param title      主题标题
     * @param content    主题内容
     * @param userId     主题发布人id
     * @param forumId    板块id
     * @param createTime 创建时间
     * @return 是否插入成功
     */
    boolean saveThread(@Param("baseDO") BaseDO baseDO, @Param("title") String title, @Param("content") String content,
                       @Param("userId") long userId, @Param("forumId") long forumId, @Param("createTime") Date createTime);

    /**
     * 根据 主题id 更新字段
     *
     * @param threadId   主题id
     * @param title      主题标题
     * @param content    主题内容
     * @param userId     主题修改人id
     * @param forumId    板块id
     * @param modifyTime 修改时间
     * @return 是否更新成功
     */
    boolean updateThread(@Param("threadId") long threadId, @Param("title") String title, @Param("content") String content,
                         @Param("userId") long userId, @Param("forumId") long forumId, @Param("modifyTime") Date modifyTime);

    IDataSet getThreadById(@Param("threadId") long threadId);

    List<IDataSet> getPosts(@Param("threadId") long threadId);

}
