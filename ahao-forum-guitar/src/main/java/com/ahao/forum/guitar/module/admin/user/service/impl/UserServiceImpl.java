package com.ahao.forum.guitar.module.admin.user.service.impl;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.module.admin.user.dao.UserMapper;
import com.ahao.forum.guitar.module.admin.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public IDataSet getProfile(Long userId, String... fields) {
        if(userId == null || userId <= 0){
            logger.debug("用户id不合法, 获取信息失败:" + userId);
            return null;
        }
        IDataSet data = userMapper.selectProfileByUserId(userId, fields);
        return data;
    }

    @Override
    public JSONObject getUsersWithPage(Integer page, String search) {
        JSONObject result = new JSONObject();
        // 1. 数据初始化
        if(page == null || page < 1){
            page = 1;
        }
        int pageSize = PageContext.getPageSize();

        // 2. 根据页数获取数据
//        IDataSet currentUser = userMapper.selectByUsername()
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = userMapper.getUsersByWeight(search, 1, "");
        result.put("list", list);

        // 3. 获取分页器
        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PageIndicator.getBootstarap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);
        return result;
    }
}
