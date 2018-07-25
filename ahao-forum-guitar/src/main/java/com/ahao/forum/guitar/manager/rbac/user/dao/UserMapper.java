package com.ahao.forum.guitar.manager.rbac.user.dao;

import com.ahao.commons.entity.BaseDO;
import com.ahao.commons.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    int saveUser(@Param("idDO") BaseDO idDO,
                 @Param("username") String username, @Param("password") String password, @Param("enabled") int enabled);

    int saveProfile(@Param("userId") long userId, @Param("avatarUrl") String avatarUrl,
                    @Param("email") String email, @Param("sex") int sex,
                    @Param("qq") String qq, @Param("city") String city);

    boolean updateUserAndProfile(@Param("userId") long userId,
                                 @Param("password") String password, @Param("enabled") int enabled,
                                 @Param("avatarUrl") String avatarUrl,
                                 @Param("email") String email, @Param("sex") int sex,
                                 @Param("qq") String qq, @Param("city") String city);

    int relateUserRole(@Param("userId") long userId, @Param("roleIds") long... roleIds);

    int relateUserCategory(@Param("userId") long userId, @Param("categoriesIds") long... categoriesIds);

    int relateUserForum(@Param("userId") long userId, @Param("forumIds") long... forumIds);

    /**
     * 删除 用户
     *
     * @param userIds 用户id数组
     * @return 返回删除记录条数
     */
    int deleteUser(@Param("userIds") long... userIds);

    boolean checkUserExists(@Param("userId") long userId);

    List<IDataSet> getUsersTableByWeight(@Param("weight") int weight, @Param("search") String search);


    IDataSet getUser(@Param("userId") long userId);

    List<IDataSet> getSelectedRoles(@Param("userId") long userId, @Param("weight") int weight);

    List<IDataSet> getSelectedCategories(@Param("userId") long userId);

    List<IDataSet> getSelectedForums(@Param("userId") long userId, @Param("isRoot") boolean isRoot, @Param("operatorUserId") long operatorUserId);


}
