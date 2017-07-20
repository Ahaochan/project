package com.ahao.invoice.admin.role.service.impl;

import com.ahao.context.PageContext;
import com.ahao.invoice.admin.role.dao.RoleDAO;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.service.impl.DataServiceImpl;
import com.ahao.service.impl.PageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Avalon on 2017/6/3.
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
    public void addRelate(Long userId, Long[] roleIds) {
        if(userId == null || roleIds==null || roleIds.length<=0){
            logger.warn("用户角色表添加失败, 用户id或角色id为空");
            return;
        }
        roleDAO.addRelate(userId, roleIds);
    }
}
