package com.ahao.spring.boot.mybatis.plus;

import com.ahao.spring.boot.Starter;
import com.ahao.spring.boot.mybatis.plus.module.entity.User;
import com.ahao.spring.boot.mybatis.plus.module.enums.Sex;
import com.ahao.spring.boot.mybatis.plus.module.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testDeleteById() {
        int count = userMapper.deleteById(1);
        Assert.assertEquals(1, count);

        User user = userMapper.selectById(1);
        Assert.assertNull(user);
    }

    @Test
    public void testLikeQuery() {
        List<User> leftLikeList = userMapper.selectList(new QueryWrapper<User>().likeLeft("email", "@qq.com"));
        Assert.assertEquals(5, leftLikeList.size());

        List<User> rightLikeList = userMapper.selectList(new QueryWrapper<User>().likeRight("username", "user"));
        Assert.assertEquals(5, rightLikeList.size());

        List<User> likeList = userMapper.selectList(new QueryWrapper<User>().like("email", "%qq%"));
        Assert.assertEquals(5, likeList.size());
    }

    @Test
    public void testEnumQuery() {
        List<User> manList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "1"));
        Assert.assertEquals(2, manList.size());
        manList.forEach(u -> Assert.assertEquals(Sex.man, u.getSex()));

        List<User> womanList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "2"));
        Assert.assertEquals(3, womanList.size());
        womanList.forEach(u -> Assert.assertEquals(Sex.woman, u.getSex()));
    }

}
