package com.ahao.forum.guitar.manager.rbac.user.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    /**
     * 删除 用户
     * @param userIds 用户id数组
     * @return 返回删除记录条数
     */
    int deleteUser(@Param("userIds") long... userIds);

    boolean checkUserExists(@Param("userId") long userId);

    int getMaxWeightByUserId(@Param("userId") long userId);

    List<IDataSet> getUsersTableByWeight(@Param("weight") int weight, @Param("search") String search);
}
