package com.ahao.forum.guitar.manager.category.dao;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    /**
     * 插入一条 分区数据
     * @param baseDO 封装id的实体类
     * @param name 分区名称
     * @param description 分区描述
     * @param status 分区状态
     * @return 是否插入成功
     */
    boolean saveCategory(@Param("baseDO") BaseDO baseDO,
                         @Param("name") String name, @Param("description") String description, @Param("status") int status);

    /**
     * 根据 分区id 更新字段
     * @param categoryId 分区id
     * @param name 分区名称
     * @param description 分区描述
     * @param status 分区状态
     * @return 是否更新成功
     */
    boolean updateCategory(@Param("categoryId") long categoryId,
                           @Param("name") String name, @Param("description") String description, @Param("status") int status);

    /**
     * 关联 分区 和 板块
     * @param categoryId 分区id
     * @param forumIds 板块id
     * @return 是否关联成功
     */
    boolean relateCategoryForum(@Param("categoryId") long categoryId, @Param("forumIds") long... forumIds);

    /**
     * 为所有最高管理员 关联 分区
     * @param categoryId 分区id
     * @return 是否关联成功
     */
    boolean relateRootCategory(@Param("categoryId") long categoryId);

    /**
     * 删除 分区
     * @param categoryIds 分区id数组
     * @return 返回删除记录条数
     */
    int deleteCategory(@Param("categoryIds") long... categoryIds);

    /**
     * 获取该分区下所有的板块, 标记起来
     * @param categoryId 分区id
     */
    List<IDataSet> getSelectedForumsByCategoryId(@Param("categoryId") long categoryId);

    /**
     * 获取userId用户拥有的所有分区
     * @param userId 用户id
     * @param search 查找分区名
     */
    List<IDataSet> getCategoriesByUserId(@Param("userId") long userId, @Param("search") String search);

    /**
     * 根据 分区id 获取 分区 的基本信息
     * @param categoryId 分区id
     * @param fields category表的字段
     */
    IDataSet getCategoryById(@Param("id") long categoryId, @Param("fields") String... fields);
}
