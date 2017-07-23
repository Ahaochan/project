package com.ahao.invoice.admin.role.service.impl;

import com.ahao.invoice.admin.role.dao.RoleDAO;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.service.impl.PageServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Avalon on 2017/6/3.
 *
 * 角色的Service层接口默认实现类
 */
@Service
public class RoleServiceImpl extends PageServiceImpl<RoleDO> implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    protected Mapper<RoleDO> dao() {
        return roleDAO;
    }

    @Override
    protected Collection<RoleDO> getByPage(int start, int pageSize, String sort, String order) {
        return roleDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public Map<RoleDO, Boolean> getSelectedRole(String... roleIds) {
        Long[] ids = Stream.of(roleIds).map(Long::parseLong).toArray(Long[]::new);
        Set<RoleDO> allRole = roleDAO.selectAllNameAndEnabled();
        Map<RoleDO, Boolean> roles = allRole.stream()
                .collect(Collectors.toMap(r -> r, r -> ArrayUtils.contains(ids, r.getId())));
        return roles;
    }

    @Override
    public Map<RoleDO, Boolean> getSelectedRole(Long userId) {
        if(userId==null) {
            logger.warn("用户id不能为空");
            return null;
        }
        Set<RoleDO> allRole = roleDAO.selectAllNameAndEnabled();
        Set<RoleDO> selectRole = roleDAO.selectNameByUserId(userId);
        Map<RoleDO, Boolean> roles = allRole.stream()
                .collect(Collectors.toMap(r->r, r->!selectRole.add(r)));
        return roles;
    }

    @Override
    public void addRelate(Long userId, String[] roleIds) {
        if(userId == null || roleIds==null || roleIds.length<=0){
            logger.warn("用户角色表添加失败, 用户id或角色id为空");
            return;
        }
        Long[] ids = Stream.of(roleIds).map(Long::parseLong).toArray(Long[]::new);
        roleDAO.addRelate(userId, ids);
    }

    @Override
    public int deleteByKey(Object roleId) {
        return roleDAO.deleteByKey((Long) roleId);
    }
}
