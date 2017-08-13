package com.ahao.invoice.admin.auth.dao;

import com.ahao.invoice.admin.auth.entity.AuthDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Avalon on 2017/5/9.
 * <p>
 * 权限的DAO层
 */
@Repository
public interface AuthDAO extends Mapper<AuthDO> {

    /**
     * 权限名是否存在
     *
     * @param name 权限名
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
    Collection<AuthDO> selectPage(@Param("start") Integer start,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("sort") String sort,
                                  @Param("order") String order);

    /**
     * 根据角色id查找角色所拥有的权限, 用于角色详情页面, 筛选所拥有的权限
     *
     * @param roleId 角色id
     * @return 角色集合
     */
    List<Map<String, Object>> selectNameByRoleId(@Param("roleId") Long roleId);

    /**
     * 修改角色权限表, 增加多对多关系, 用于角色详情页面
     *
     * @param roleId  角色的id
     * @param authIds 角色所拥有的权限的id
     */
    void addRelate(@Param("roleId") Long roleId, @Param("authIds") Long[] authIds);

    /**
     * Spring Security权限验证, 获取登录所需权限数据
     *
     * @param userId 根据用户id查找权限数据
     * @return 权限数据集合
     */
    Set<AuthDO> selectNameByUserId(@Param("userId") Long userId);
}
