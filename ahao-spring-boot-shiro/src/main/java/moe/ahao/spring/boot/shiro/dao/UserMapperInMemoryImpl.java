package moe.ahao.spring.boot.shiro.dao;

import moe.ahao.spring.boot.shiro.entity.ShiroUser;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class UserMapperInMemoryImpl implements UserMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserMapperInMemoryImpl.class);
    @Override
    public ShiroUser selectByUsernameOrEmail(String principal) {
        for (ShiroUser shiroUser : datasource) {
            if (Objects.equals(principal, shiroUser.getUsername()) || Objects.equals(principal, shiroUser.getEmail())) {
                logger.debug("模拟数据库查询, 结果为:{}", shiroUser.toString());
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
            user.setUsername("admin" + i);
            user.setEmail(i + "@qq.com");
            String hashedPassword = (new Sha512Hash("pw" + i, new Sha1Hash("salt" + i), 1024)).toString();
            user.setPassword(hashedPassword);
            user.setSalt("salt" + i);

            user.setLocked(false);
            user.setDisabled(false);
            user.setDeleted(false);

            user.setExpireTime(null);
            user.setCreateTime(now);
            user.setUpdateTime(now);
            datasource.add(user);
        }
    }
}
