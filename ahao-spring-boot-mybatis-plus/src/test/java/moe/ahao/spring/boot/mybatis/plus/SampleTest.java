package moe.ahao.spring.boot.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.ahao.spring.boot.Starter;
import moe.ahao.transaction.AbstractUserTest;
import moe.ahao.transaction.user.mybatis.entity.User;
import moe.ahao.transaction.user.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test")

// @ContextConfiguration(classes = {MyBatisPlusConfig.class, MultiMyBatisConfig.class,
//     DataSourceAutoConfiguration.class,
//     MybatisPlusAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
class SampleTest extends AbstractUserTest {

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
