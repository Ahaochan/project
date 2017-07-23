package com.ahao.invoice.admin.auth.service.impl;

import com.ahao.invoice.admin.auth.dao.AuthDAO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
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
 * Created by Avalon on 2017/6/7.
 */
@Service
public class AuthServiceImpl extends PageServiceImpl<AuthDO> implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private AuthDAO authDAO;

    @Autowired
    public AuthServiceImpl(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    protected Mapper<AuthDO> dao() {
        return authDAO;
    }

    @Override
    protected Collection<AuthDO> getByPage(int start, int pageSize, String sort, String order) {
        return authDAO.selectPage(start, pageSize, sort, order);
    }


    @Override
    public Map<AuthDO, Boolean> getSelectedAuth(String... authIds) {
        Long[] ids = Stream.of(authIds).map(Long::parseLong).toArray(Long[]::new);
        Set<AuthDO> allAuth = authDAO.selectAllNameAndEnabled();
        Map<AuthDO, Boolean> roles = allAuth.stream()
                .collect(Collectors.toMap(a -> a, a -> ArrayUtils.contains(ids, a.getId())));
        return roles;
    }

    @Override
    public Map<AuthDO, Boolean> getSelectedAuth(Long roleId) {
        if(roleId==null) {
            logger.warn("角色id不能为空");
            return null;
        }

        Set<AuthDO> allRole = authDAO.selectAllNameAndEnabled();
        Set<AuthDO> selectRole = authDAO.selectNameByRoleId(roleId);

        Map<AuthDO, Boolean> auths = allRole.stream()
                .collect(Collectors.toMap(r->r, r->!selectRole.add(r)));
        return auths;
    }

    @Override
    public void addRelate(Long roleId, String[] authIds) {
        if(roleId == null || authIds==null || authIds.length<=0){
            logger.warn("角色权限表添加失败, 用户id或权限id为空");
            return;
        }
        Long[] ids = Stream.of(authIds).map(Long::parseLong).toArray(Long[]::new);
        authDAO.addRelate(roleId, ids);
    }

    @Override
    public int deleteByKey(Object authId) {
        return authDAO.deleteByKey((Long) authId);
    }
}
