package com.ahao.forum.guitar.manager.rbac.user.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    int getMaxWeightByUserId(@Param("userId") long userId);

    List<IDataSet> getUsersTableByWeight(@Param("weight") int weight, @Param("search") String search);
}
