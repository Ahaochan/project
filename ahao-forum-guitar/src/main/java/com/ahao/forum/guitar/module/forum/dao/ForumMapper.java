package com.ahao.forum.guitar.module.forum.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumMapper {

    List<IDataSet> getForums(@Param("fields") String... fields);

}
