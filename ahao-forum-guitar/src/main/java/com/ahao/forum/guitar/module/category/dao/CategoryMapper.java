package com.ahao.forum.guitar.module.category.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {

    List<IDataSet> getCategoriesByUserId(@Param("userId") long userId, @Param("search") String search,
                                            @Param("fields") String... fields);
    IDataSet getCategoryById(@Param("id") long categoryId, @Param("fields") String... fields);

    int deleteCategory(@Param("categoryIds") long... categoryIds);







    List<IDataSet> getCategoryByParentId(@Param("parentId") long parentId, @Param("fields") String... fields);
}
