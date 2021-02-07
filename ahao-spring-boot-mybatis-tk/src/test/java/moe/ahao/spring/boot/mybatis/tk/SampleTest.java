package moe.ahao.spring.boot.mybatis.tk;

import moe.ahao.spring.boot.mybatis.tk.config.MapperConig;
import moe.ahao.spring.boot.mybatis.tk.module.entity.User;
import moe.ahao.spring.boot.mybatis.tk.module.mapper.UserMapper;
import org.apache.ibatis.cursor.Cursor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {MapperConig.class,
    DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, TransactionAutoConfiguration.class})
@ActiveProfiles("test")
class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testDeleteById() {
        int count = userMapper.deleteByPrimaryKey(1);
        Assertions.assertEquals(1, count);

        User user = userMapper.selectByPrimaryKey(1);
        Assertions.assertNull(user);
    }

    @Test
    void testLikeQuery() {
        Example.Builder builder = new Example.Builder(User.class);

        List<User> leftLikeList = userMapper.selectByExample(builder.where(WeekendSqls.<User>custom().andLike(User::getEmail, "%@qq.com")).build());
        Assertions.assertEquals(5, leftLikeList.size());

        List<User> rightLikeList = userMapper.selectByExample(builder.where(WeekendSqls.<User>custom().andLike(User::getUsername, "user%")).build());
        Assertions.assertEquals(5, rightLikeList.size());

        List<User> likeList = userMapper.selectByExample(builder.where(WeekendSqls.<User>custom().andLike(User::getEmail, "%qq%")).build());
        Assertions.assertEquals(5, likeList.size());
    }

    @Test
    void testEnumQuery() {
        // List<User> manList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "1"));
        // Assertions.assertEquals(2, manList.size());
        // manList.forEach(u -> Assertions.assertEquals(Sex.man, u.getSex()));
        //
        // List<User> womanList = userMapper.selectList(new QueryWrapper<User>().eq("sex", "2"));
        // Assertions.assertEquals(3, womanList.size());
        // womanList.forEach(u -> Assertions.assertEquals(Sex.woman, u.getSex()));
    }

    @Test
    void testInsert() {
        User user1 = new User();
        user1.setUsername("username");
        user1.setPassword("password");
        int count = userMapper.insert(user1);
        Assertions.assertEquals(1, count);
        Assertions.assertNotNull(user1.getId());

        User user2 = userMapper.selectByPrimaryKey(user1.getId());
        Assertions.assertEquals(user1.getUsername(), user2.getUsername());
        Assertions.assertEquals(user1.getPassword(), user2.getPassword());
    }

    @Test
    @Transactional
    void scanFoo0() throws Exception {
        try (Cursor<User> cursor = userMapper.selectCursorAll()) {
            Iterator<User> it = cursor.iterator();

            while (it.hasNext()) {
                List<User> userList = new ArrayList<>();
                for (int i = 0; i < 3 && it.hasNext(); i++) {
                    userList.add(it.next());
                }
                System.out.println("读取" + userList.size() + "条记录, " + userList);
            }
        }
    }
}
