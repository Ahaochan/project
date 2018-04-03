package com.ahao.forum.guitar.manager.rbac.auth.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthMapper {

    /**
     * 根据用户id获取用户所拥有的权限信息
     * @param userId 用户id
     * @param fields admin_auth的字段
     * @return 权限信息
     */
    List<IDataSet> getByUserId(@Param("userId") Long userId, @Param("fields") String... fields);
}
