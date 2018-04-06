package com.ahao.forum.guitar.manager.rbac.role.service.impl;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.manager.rbac.role.dao.RoleMapper;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private RoleMapper roleMapper;
    public RoleServiceImpl(RoleMapper roleMapper){
        this.roleMapper = roleMapper;
    }

    @Transactional
    @Override
    public long saveRole(Long roleId, String name, String description, Integer weight, Integer status, Long... authIds) {
        // 1. 数据初始化
        if(weight == null || weight < 0){
            weight = 0;
        }
        if (status == null || status < 0) {
            status = 1;
        }
        // 2. 如果 roleId 不存在, 则插入数据
        if (roleId == null || roleId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            roleMapper.saveRole(idDO, name, description, weight, status, new Date());
            roleId = idDO.getId();
            // 2.2. 插入失败则返回 false
            if (roleId == null || roleId < 0) {
                return -1;
            }
        }
        // 3. 如果 roleId 存在, 则更新数据, 并添加联系
        else {
            // 3.1. 更新数据
            boolean success = roleMapper.updateRole(roleId, name, description, weight, status, new Date());
        }

        // 4. 添加角色权限的联系
        roleMapper.relateRoleAuth(roleId, NumberHelper.unboxing(authIds));

        return roleId;
    }

    @Override
    public boolean deleteRole(Long... roleIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long roleId : roleIds) {
            if (roleId == null || roleId <= 0) {
                continue;
            }
            IDataSet data = roleMapper.getRoleById(roleId, "id");
            if (data != null) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = roleMapper.deleteRole(NumberHelper.unboxing(roleIds));
            return true;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除角色失败, 数据表中不存在id:" + Arrays.toString(roleIds) + "的记录");
        return false;
    }

    @Override
    public List<IDataSet> getRoles(String search) {
        List<IDataSet> list = roleMapper.getRoles(search);
        return list;
    }

    @Override
    public IDataSet getRole(Long roleId) {
        if (roleId == null || roleId <= 0) {
            logger.debug("角色id非法:" + roleId);
            return null;
        }
        IDataSet data = roleMapper.getRoleById(roleId);
        return data;
    }

    @Override
    public List<IDataSet> getSelectedAuths(Long roleId) {
        List<IDataSet> list = roleMapper.getSelectedAuthsByRoleId(roleId);
        return list;
    }
}
