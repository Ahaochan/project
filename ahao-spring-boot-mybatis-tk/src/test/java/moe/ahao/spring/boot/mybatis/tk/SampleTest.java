package moe.ahao.spring.boot.mybatis.tk;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.mybatis.tk.module.entity.UserTK;
import moe.ahao.spring.boot.mybatis.tk.module.mapper.UserTKMapper;
import moe.ahao.transaction.AbstractUserTest;
import org.apache.ibatis.cursor.Cursor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test")
// @ContextConfiguration(classes = {MultiMyBatisConfig.class,
//     DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class,
//     DataSourceTransactionManagerAutoConfiguration.class, TransactionAutoConfiguration.class})
class SampleTest extends AbstractUserTest {
    @Autowired
    private UserTKMapper userTKMapper;

    @Test
    void testDeleteById() {
        int count = userTKMapper.deleteByPrimaryKey(1);
        Assertions.assertEquals(1, count);

        UserTK user = userTKMapper.selectByPrimaryKey(1);
        Assertions.assertNull(user);
    }

    @Test
    void testLikeQuery() {
        Example.Builder builder = new Example.Builder(UserTK.class);

        List<UserTK> leftLikeList = userTKMapper.selectByExample(builder.where(WeekendSqls.<UserTK>custom().andLike(UserTK::getEmail, "%@qq.com")).build());
        Assertions.assertEquals(5, leftLikeList.size());

        List<UserTK> rightLikeList = userTKMapper.selectByExample(builder.where(WeekendSqls.<UserTK>custom().andLike(UserTK::getUsername, "user%")).build());
        Assertions.assertEquals(5, rightLikeList.size());

        List<UserTK> likeList = userTKMapper.selectByExample(builder.where(WeekendSqls.<UserTK>custom().andLike(UserTK::getEmail, "%qq%")).build());
        Assertions.assertEquals(5, likeList.size());
    }

    @Test
    void testInsert() {
        UserTK user1 = new UserTK();
        user1.setUsername("username");
        user1.setPassword("password");
        int count = userTKMapper.insert(user1);
        Assertions.assertEquals(1, count);
        Assertions.assertNotNull(user1.getId());

        UserTK user2 = userTKMapper.selectByPrimaryKey(user1.getId());
        Assertions.assertEquals(user1.getUsername(), user2.getUsername());
        Assertions.assertEquals(user1.getPassword(), user2.getPassword());
    }

    @Test
    @Transactional
    void scanFoo0() throws Exception {
        try (Cursor<UserTK> cursor = userTKMapper.selectCursorAll()) {
            Iterator<UserTK> it = cursor.iterator();

            while (it.hasNext()) {
                List<UserTK> userList = new ArrayList<>();
                for (int i = 0; i < 3 && it.hasNext(); i++) {
                    userList.add(it.next());
                }
                System.out.println("读取" + userList.size() + "条记录, " + userList);
            }
        }
    }
}
