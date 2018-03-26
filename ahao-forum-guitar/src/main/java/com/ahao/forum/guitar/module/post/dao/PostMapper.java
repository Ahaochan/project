package com.ahao.forum.guitar.module.post.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMapper {
    List<IDataSet> getPostByCategoryId(@Param("categoryId") long categoryId, @Param("search") String search);
}
