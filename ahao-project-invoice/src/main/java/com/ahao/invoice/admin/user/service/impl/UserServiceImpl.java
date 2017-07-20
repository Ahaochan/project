package com.ahao.invoice.admin.user.service.impl;

import com.ahao.context.PageContext;
import com.ahao.entity.DropDownListDTO;
import com.ahao.invoice.admin.user.dao.UserDAO;
import com.ahao.invoice.admin.user.entity.UserDO;
import com.ahao.invoice.admin.user.service.UserService;
import com.ahao.service.impl.DataServiceImpl;
import com.ahao.service.impl.PageServiceImpl;
import com.ahao.util.SecurityUtils;
import com.ahao.util.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Avalon on 2017/5/11.
 */
@Service
public class UserServiceImpl extends PageServiceImpl<UserDO> implements UserService {
    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected Mapper<UserDO> dao() {
        return userDAO;
    }

    @Override
    protected Collection<UserDO> getByPage(int start, int pageSize, String sort, String order) {
        return userDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public boolean updateLoginMsg(String username) {
        return userDAO.updateLastLoginMsg(username,
                new Date(), SecurityUtils.getClientIp());
    }

}
