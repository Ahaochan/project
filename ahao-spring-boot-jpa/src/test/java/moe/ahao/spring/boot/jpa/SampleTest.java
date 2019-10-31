package moe.ahao.spring.boot.jpa;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.jpa.module.entity.User;
import moe.ahao.spring.boot.jpa.module.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testNativeSql() {
        int count = userMapper.getCount();
        Assertions.assertEquals(5, count);
    }

    @Test
    void testLikeQuery() {
        List<User> leftLikeList = userMapper.getUsersByUsernameLike("user%");
        Assertions.assertEquals(5, leftLikeList.size());
    }

    @Test
    @Transactional
    void testInsert() {
        User user1 = new User();
        user1.setUsername("username");
        user1.setPassword("password");
        User user2 = userMapper.save(user1);
        System.out.println(user2.getId());
        Assertions.assertNotNull(user2.getId());

        User user3 = userMapper.getOne(user2.getId());
        Assertions.assertEquals(user1.getUsername(), user3.getUsername());
        Assertions.assertEquals(user1.getPassword(), user3.getPassword());
    }
}
