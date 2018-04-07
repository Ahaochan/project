package com.ahao.forum.guitar.manager.rbac.auth.service.impl;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.manager.rbac.auth.dao.AuthMapper;
import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private AuthMapper authMapper;
    public AuthServiceImpl(AuthMapper authMapper){
        this.authMapper = authMapper;
    }


    @Transactional
    @Override
    public long saveAuth(Long authId, String name, String description, Integer enabled) {
        // 1. 数据初始化
        if (enabled == null || enabled < 0) {
            enabled = 1;
        }
        // 2. 如果 authId 不存在, 则插入数据
        if (authId == null || authId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            authMapper.saveAuth(idDO, name, description, enabled, new Date());
            authId = idDO.getId();
            // 2.2. 插入失败则返回 false
            if (authId == null || authId < 0) {
                return -1;
            }
            // 2.3. 为所有最高管理员添加该权限
            int maxWeight = ShiroHelper.getMaxWeight();
            authMapper.relateRootAuth(authId, maxWeight);
        }
        // 3. 如果 authId 存在, 则更新数据
        else {
            // 3.1. 更新数据
            boolean success = authMapper.updateAuth(authId, name, description, enabled, new Date());
        }
        return authId;
    }

    @Override
    public int deleteAuth(Long... authIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long authId : authIds) {
            if (authId == null || authId <= 0) {
                continue;
            }
            IDataSet data = authMapper.getAuthById(authId, "id");
            if (data != null) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = authMapper.deleteAuth(NumberHelper.unboxing(authIds));
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除权限失败, 数据表中不存在id:" + Arrays.toString(authIds) + "的记录");
        return 0;
    }

    @Override
    public IDataSet getAuth(Long authId) {
        if (authId == null || authId <= 0) {
            logger.debug("权限id非法:" + authId);
            return null;
        }
        IDataSet data = authMapper.getAuthById(authId);
        return data;
    }

    @Override
    public List<IDataSet> getAuths(String search) {
        // TODO 需要用户管理的权限
        List<IDataSet> list = authMapper.getAuths(search);
        return list;
    }
}
