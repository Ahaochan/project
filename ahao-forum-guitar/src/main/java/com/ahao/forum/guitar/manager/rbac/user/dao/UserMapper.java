package com.ahao.forum.guitar.manager.rbac.user.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    IDataSet selectUserByUserId(@Param("userId") long userId, @Param("fields") String... fields );

    IDataSet selectProfileByUserId(@Param("userId") long userId, @Param("fields") String... fields);




    List<IDataSet> getUsersByWeight(@Param("search") String search, @Param("weight") int weight, @Param("fields") String... fields);
}
