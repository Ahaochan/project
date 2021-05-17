package moe.ahao.web.module.user.service.impl;

import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.mapper.UserMapper;
import moe.ahao.web.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        if(id == null || id < 0) {
            return null;
        }
        User user = userMapper.selectById(id);
        return user;
    }
}
