package moe.ahao.web.module.user.service.impl;

import moe.ahao.web.module.user.controller.dto.UserSaveDTO;
import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.mapper.UserMapper;
import moe.ahao.web.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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

    @Override
    public Long save(UserSaveDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setSalt(UUID.randomUUID().toString());
        user.setCreateBy(1L);
        user.setUpdateBy(1L);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        if(user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }
        return user.getId();
    }
}
