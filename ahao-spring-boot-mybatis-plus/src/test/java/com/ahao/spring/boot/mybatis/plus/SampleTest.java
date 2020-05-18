package com.ahao.spring.boot.mybatis.plus;

import com.ahao.spring.boot.mybatis.plus.config.MyBatisPlusConfig;
import com.ahao.spring.boot.mybatis.plus.module.entity.User;
import com.ahao.spring.boot.mybatis.plus.module.enums.Sex;
import com.ahao.spring.boot.mybatis.plus.module.mapper.UserMapper;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {MyBatisPlusConfig.class,
    DataSourceAutoConfiguration.class,
    MybatisPlusAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
@ActiveProfiles("test")
class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testDeleteById() {
        int count = userMapper.deleteById(1);
        Assertions.assertEquals(1, count);

        User user = userMapper.selectById(1);
        Assertions.assertNull(user);
    }

    @Test
    void testLikeQuery() {
        List<User> leftLikeList = userMapper.selectList(new QueryWrapper<User>().likeLeft("email", "@qq.com"));
        Assertions.assertEquals(5, leftLikeList.size());

        List<User> rightLikeList = userMapper.selectList(new QueryWrapper<User>().likeRight("username", "user"));
        Assertions.assertEquals(5, rightLikeList.size());

        List<User> likeList = userMapper.selectList(new QueryWrapper<User>().like("email", "qq"));
        Assertions.assertEquals(5, likeList.size());
    }

    @Test
    void testEnumQuery() {
        List<User> manList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "1"));
        Assertions.assertEquals(2, manList.size());
        manList.forEach(u -> Assertions.assertEquals(Sex.man, u.getSex()));

        List<User> womanList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "2"));
        Assertions.assertEquals(3, womanList.size());
        womanList.forEach(u -> Assertions.assertEquals(Sex.woman, u.getSex()));
    }

    @Test
    void testInsert() {
        User user1 = new User();
        user1.setUsername("username");
        user1.setPassword("password");
        int count = userMapper.insert(user1);
        Assertions.assertEquals(1, count);
        Assertions.assertNotNull(user1.getId());

        User user2 = userMapper.selectById(user1.getId());
        Assertions.assertEquals(user1.getUsername(), user2.getUsername());
        Assertions.assertEquals(user1.getPassword(), user2.getPassword());
    }
}
