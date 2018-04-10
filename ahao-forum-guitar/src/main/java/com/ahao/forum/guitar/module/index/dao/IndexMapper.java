package com.ahao.forum.guitar.module.index.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexMapper {
    List<IDataSet> getCategories(@Param("status") int status);
    List<IDataSet> getForumsByCategoryId(@Param("categoryId") long categoryId);
}
