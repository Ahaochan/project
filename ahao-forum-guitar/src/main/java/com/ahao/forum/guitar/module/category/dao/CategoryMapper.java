package com.ahao.forum.guitar.module.category.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<IDataSet> getCategory(@Param("parentId") long parentId, @Param("fields") String... fields);
}
