package com.ahao.forum.guitar.manager.rbac.user.service.impl;

import com.ahao.core.util.lang.StringHelper;
import com.ahao.forum.guitar.manager.rbac.user.dao.PasswordMapper;
import com.ahao.forum.guitar.manager.rbac.user.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);


    private PasswordMapper passwordMapper;

    @Autowired
    public PasswordServiceImpl(PasswordMapper passwordMapper) {
        this.passwordMapper = passwordMapper;
    }

    @Override
    public String modifyPassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || userId <= 0) {
            String msg = "修改密码失败, userId非法";
            logger.debug(msg + ":" + userId);
            return msg;
        }
        if (StringHelper.isEmpty(oldPassword, newPassword)) {
            String msg = "修改密码失败, password为空";
            logger.debug(msg + ":" + oldPassword + "," + newPassword);
            return msg;
        }
        if (oldPassword.equals(newPassword)) {
            String msg = "修改密码失败, 新旧密码一致";
            logger.debug(msg + ":" + oldPassword + "," + newPassword);
            return msg;
        }

        String password = passwordMapper.getOldPassword(userId);
        if (!oldPassword.equals(password)) {
            String msg = "修改密码失败, 旧密码不匹配";
            logger.debug(msg + ":" + oldPassword + "," + newPassword);
            return msg;
        }

        boolean success = passwordMapper.updatePassword(userId, newPassword);

        return success ? "" : "修改密码失败";
    }
}
