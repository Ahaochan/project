package com.ahao.invoice.admin.role.service.impl;

import com.ahao.config.SpringConfig;
import com.ahao.invoice.admin.role.dao.RoleDAO;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.service.impl.PageServiceImpl;
import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Avalon on 2017/6/3.
 * <p>
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
    protected Class<RoleDO> clazz() {
        return RoleDO.class;
    }

    @Override
    protected Collection<RoleDO> getByPage(int start, int pageSize, String sort, String order) {
        return roleDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public boolean existName(String name) {
        if (StringHelper.isEmpty(name)) {
            return false;
        }

        return roleDAO.existName(name);
    }

    @Override
    public JSONArray getSelectedRole(Long userId) {
        JSONArray json = new JSONArray();
        List<Map<String, Object>> list = roleDAO.selectNameByUserId(userId);
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
    public void addRelate(Long userId, Long[] roleIds) {
        if (userId == null) {
            logger.warn("用户角色表添加失败, 用户id为空");
            return;
        }

        System.out.println("测试:"+userId+","+ Arrays.toString(roleIds));
        roleDAO.addRelate(userId, roleIds);
    }
}
