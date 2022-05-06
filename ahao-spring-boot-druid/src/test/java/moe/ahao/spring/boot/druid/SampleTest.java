package moe.ahao.spring.boot.druid;

import moe.ahao.spring.boot.Starter;
import moe.ahao.transaction.AbstractUserTest;
import moe.ahao.transaction.user.mybatis.entity.User;
import moe.ahao.transaction.user.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test")
class SampleTest extends AbstractUserTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void selectById() {
        User user1 = userMapper.selectById(1);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(1, user1.getId().intValue());
        Assertions.assertEquals("user1", user1.getUsername());

        User user2 = userMapper.selectById(2);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals(2, user2.getId().intValue());
        Assertions.assertEquals("user2", user2.getUsername());

        User user3 = userMapper.selectById(999);
        Assertions.assertNull(user3);
    }
}

