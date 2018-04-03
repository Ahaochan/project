package com.ahao.forum.guitar.manager.rbac.user.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper {
    boolean updatePassword(@Param("userId") long userId, @Param("password") String password);
    boolean updateLastLoginMsg(@Param("lastLoginTime") Date lastLoginTi0me, @Param("lastLoginIp") String lastLoginIp,
                               @Param("userId") long userId);

    IDataSet selectUserByUsername(@Param("username") String username, @Param("fields") String... fields);
    IDataSet selectUserByUserId(@Param("userId") long userId, @Param("fields") String... fields );

    IDataSet selectProfileByUserId(@Param("userId") long userId, @Param("fields") String... fields);




    List<IDataSet> getUsersByWeight(@Param("search") String search, @Param("weight") int weight, @Param("fields") String... fields);
}
