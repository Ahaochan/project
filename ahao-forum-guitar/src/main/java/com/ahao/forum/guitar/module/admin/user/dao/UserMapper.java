package com.ahao.forum.guitar.module.admin.user.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper {

    boolean updateLastLoginMsg(@Param("lastLoginTime") Date lastLoginTi0me, @Param("lastLoginIp") String lastLoginIp,
                               @Param("userId") long userId);

    IDataSet selectProfileByUserId(@Param("userId") long userId, @Param("fields") String... fields);

    IDataSet selectByUsername(@Param("username") String username, @Param("fields") String... fields);


    List<IDataSet> getUsersByWeight(@Param("search") String search, @Param("weight") int weight, @Param("fields") String... fields);
}
