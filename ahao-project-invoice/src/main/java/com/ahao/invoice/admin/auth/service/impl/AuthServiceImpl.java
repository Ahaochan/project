package com.ahao.invoice.admin.auth.service.impl;

import com.ahao.config.SpringConfig;
import com.ahao.invoice.admin.auth.dao.AuthDAO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
import com.ahao.service.impl.PageServiceImpl;
import com.ahao.util.ArrayHelper;
import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    protected Class<AuthDO> clazz() {
        return AuthDO.class;
    }

    @Override
    protected Collection<AuthDO> getByPage(int start, int pageSize, String sort, String order) {
        return authDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public boolean existName(String name) {
        if (StringHelper.isEmpty(name)) {
            return false;
        }

        Example example = new Example(AuthDO.class);
        example.createCriteria().andEqualTo("name", name);
        int count = authDAO.selectCountByExample(example);
        return count > 0;
    }

    @Override
    public JSONArray getSelectedAuth(Long roleId) {
        JSONArray json = new JSONArray();
        List<Map<String, Object>> list = authDAO.selectNameByRoleId(roleId);
        for (Map<String, Object> data : list) {
            JSONObject item = new JSONObject();
            item.put("id", data.get("id"));
            item.put("name", SpringConfig.getString(data.get("name").toString()));
            item.put("enabled", data.get("enabled"));
            item.put("selected", data.get("selected"));
            json.add(item);
        }
        return json;
    }

    @Override
    public void addRelate(Long roleId, Long[] authIds) {
        if (roleId == null) {
            logger.warn("角色权限表添加失败, 角色id为空");
            return;
        }
        logger.debug("添加角色权限表关联: 角色id:["+roleId+"], 权限id"+ ArrayHelper.toString(authIds));
        authDAO.addRelate(roleId, authIds);
    }
}
