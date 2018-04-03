package com.ahao.forum.guitar.manager.rbac.user.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordMapper {
    boolean updatePassword(@Param("userId") long userId, @Param("password") String password);

    String getOldPassword(@Param("userId") long userId);
}
