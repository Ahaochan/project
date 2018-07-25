package com.ahao.forum.guitar.manager.profile.dao;

import com.ahao.commons.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileMapper {
    int updateProfile(@Param("userId") long userId, @Param("avatarUrl") String avatarUrl,
                      @Param("email") String email, @Param("sex") int sex,
                      @Param("qq") String qq, @Param("city") String city);


    /**
     * 获取用户资料
     * @param userId 用户id
     */
    IDataSet getProfile(@Param("userId") long userId);

    /**
     * 根据用户id查找用户所拥有的角色, 并使用 selected 字段标记, 非0为拥有
     * @param userId 用户id
     */
    List<IDataSet> getSelectedRolesByUserId(@Param("userId") long userId);

    /**
     * 根据用户id查找用户所拥有的权限, 并使用 selected 字段标记, 非0为拥有
     * @param userId 用户id
     */
    List<IDataSet> getSelectedAuthsByUserId(@Param("userId") long userId);
}
