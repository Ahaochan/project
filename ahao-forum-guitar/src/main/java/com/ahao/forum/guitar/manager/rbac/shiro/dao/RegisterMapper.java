package com.ahao.forum.guitar.manager.rbac.shiro.dao;

import com.ahao.core.entity.BaseDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterMapper {
    boolean isExistUsername(@Param("username") String username);

    int createUser(@Param("idDO") BaseDO idDO, @Param("username") String username, @Param("password") String password);

    int createProfile(@Param("userId") long userId);
}
