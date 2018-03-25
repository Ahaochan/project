package com.ahao.forum.guitar.module.admin.role.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    List<IDataSet> selectRoleByUserId(@Param("userId") Long userId, @Param("fields") String... fields);
}
