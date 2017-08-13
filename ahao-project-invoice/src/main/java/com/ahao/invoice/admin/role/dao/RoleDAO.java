package com.ahao.invoice.admin.role.dao;

import com.ahao.invoice.admin.role.entity.RoleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Avalon on 2017/6/3.
 * 角色的DAO层
 */
@Repository
public interface RoleDAO extends Mapper<RoleDO> {

    /**
     * 角色名是否存在
     *
     * @param name 角色名
     * @return 存在为true
     */
    boolean existName(@Param("name") String name);

    /**
     * 分页查找
     *
     * @param start    从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort     按sort字段排序
     * @param order    order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
    Collection<RoleDO> selectPage(@Param("start") Integer start,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("sort") String sort,
                                  @Param("order") String order);

    /**
     * 根据用户id查找用户所拥有的角色, 用于用户详情页面, 筛选所拥有的角色
     *
     * @param userId 用户id
     * @return 角色集合
     */
    List<Map<String, Object>> selectNameByUserId(@Param("userId") Long userId);

    /**
     * 修改用户角色表, 增加多对多关系, 用于用户详情页面
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    void addRelate(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
}
