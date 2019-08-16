package moe.ahao.spring.boot.druid.module.mapper;


import moe.ahao.spring.boot.druid.module.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User selectById(@Param("id") Integer id);
}
