package com.ahao.forum.guitar.manager.rbac.shiro.service.impl;

import com.ahao.commons.entity.BaseDO;
import com.ahao.forum.guitar.manager.rbac.shiro.dao.RegisterMapper;
import com.ahao.forum.guitar.manager.rbac.shiro.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    private RegisterMapper registerMapper;
    public RegisterServiceImpl(RegisterMapper registerMapper) {
        this.registerMapper = registerMapper;
    }

    @Override
    public boolean isExistUsername(String username) {
        if(StringUtils.isEmpty(username)){
            return true;
        }
        boolean isExist = registerMapper.isExistUsername(username);
        return isExist;
    }

    @Override
    public boolean register(String username, String password) {
        // 1. BaseDO封装插入记录的id
        BaseDO idDO = new BaseDO();
        registerMapper.createUser(idDO, username, password);
        long userId = idDO.getId();
        // 2. 插入失败则返回 -1
        if (userId < 0) {
            return false;
        }
        // 3. 插入用户信息
        registerMapper.createProfile(userId);
        // 4. 绑定普通用户角色
        registerMapper.relateSimpleRole(userId);
        return true;
    }
}
