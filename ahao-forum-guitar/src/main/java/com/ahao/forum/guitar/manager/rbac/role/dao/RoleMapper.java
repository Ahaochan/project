package com.ahao.forum.guitar.manager.rbac.role.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色的DAO层
 */
@Repository
public interface RoleMapper {

    /**
     * 分页查找
     *
     * @param start    从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort     按sort字段排序
     * @param order    order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
//    List<IDataSet> selectPage(@Param("start") Integer start,
//                                  @Param("pageSize") Integer pageSize,
//                                  @Param("sort") String sort,
//                                  @Param("order") String order);


    /**
     * 根据用户id获取用户所拥有的角色信息
     * @param userId 用户id
     * @param fields admin_role的字段
     * @return 角色信息
     */
    List<IDataSet> getByUserId(@Param("userId") long userId, @Param("fields") String... fields);

    /**
     * 修改用户角色表, 增加多对多关系, 用于用户详情页面
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
//    void addRelate(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
}

