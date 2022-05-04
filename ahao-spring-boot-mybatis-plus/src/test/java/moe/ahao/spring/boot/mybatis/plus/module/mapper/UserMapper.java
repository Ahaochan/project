package moe.ahao.spring.boot.mybatis.plus.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.spring.boot.mybatis.plus.module.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void insertSQL(User user);
    void insertBatchSQL(List<User> users);
    void truncate();
}
