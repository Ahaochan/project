package com.ahao.invoice.admin.user.dao;

import com.ahao.invoice.admin.user.entity.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Avalon on 2017/5/4.
 *
 */
@Repository
public interface UserDAO extends Mapper<UserDO> {

    /**
     * Spring Security权限验证, 获取登录所需用户数据
     * @param username 根据用户名查找数据
     * @return 用户数据
     */
    UserDO loginByUsername(@Param("username") String username);

    /**
     * 分页查找
     * @param start 从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort 按sort字段排序
     * @param order order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
    Collection<UserDO> selectPage(@Param("start") Integer start,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("sort") String sort,
                                  @Param("order") String order);

    /**
     * 更新最后一次登录时间
     * @param username 根据用户名查找数据
     * @param lastLoginTime 最后一次登录时间
     * @param lastLoginIp 最后一次登录ip
     * @return true为更新成功
     */
    boolean updateLastLoginMsg(@Param("username") String username,
                               @Param("lastLoginTime") Date lastLoginTime,
                               @Param("lastLoginIp") String lastLoginIp);

    /**
     * 删除用户, 级联删除
     *
     * @param userId
     * @return
     */
    int deleteByKey(Long userId);
}
