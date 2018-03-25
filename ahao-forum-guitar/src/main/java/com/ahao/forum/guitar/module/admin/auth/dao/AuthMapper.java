package com.ahao.forum.guitar.module.admin.auth.dao;

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

    /**
     * 根据用户id查找用户所拥有的权限, 并使用 selected 字段标记, 非0为拥有
     *
     * @param userId 用户id
     * @param fields admin_auth的字段
     * @return 权限信息
     */
    List<IDataSet> getSelectedByUserId(@Param("userId") long userId, @Param("fields") String... fields);
}
