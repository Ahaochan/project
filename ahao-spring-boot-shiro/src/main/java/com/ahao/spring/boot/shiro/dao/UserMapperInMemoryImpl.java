package com.ahao.spring.boot.shiro.dao;

import com.ahao.spring.boot.shiro.entity.ShiroUser;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class UserMapperInMemoryImpl implements UserMapper {
    @Override
    public ShiroUser selectByUsernameOrEmail(String principal) {
        for (ShiroUser shiroUser : datasource) {
            if (Objects.equals(principal, shiroUser.getUsername()) || Objects.equals(principal, shiroUser.getEmail())) {
                return shiroUser;
            }
        }
        return null;
    }

    private static List<ShiroUser> datasource;
    static {
        datasource = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Date now = new Date();
            ShiroUser user = new ShiroUser();
            user.setId((long) i);
            user.setUsername("用户" + i);
            user.setEmail(i + "@qq.com");
            String hashedPassword = (new Sha512Hash("pw" + i, "salt" + i, 1024)).toString();
            user.setPassword(hashedPassword);
            user.setSalt("salt" + i);

            user.setLocked(false);
            user.setDisabled(false);
            user.setDeleted(false);

            user.setExpireTime(null);
            user.setCreateTime(now);
            user.setModifyTime(now);
            datasource.add(user);
        }
    }
}
