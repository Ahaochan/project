package moe.ahao.spring.boot.druid;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.druid.module.entity.User;
import moe.ahao.spring.boot.druid.module.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
public class SampleTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        jdbcTemplate.execute("create table sys_user(id int, username varchar(50));");
        jdbcTemplate.execute("insert into sys_user(id, username) values (1, 'name1'), (2, 'name2')");
    }

    @Test
    public void selectById() {
        User user1 = userMapper.selectById(1);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(1, user1.getId().intValue());
        Assertions.assertEquals("name1", user1.getUsername());

        User user2 = userMapper.selectById(2);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals(2, user2.getId().intValue());
        Assertions.assertEquals("name2", user2.getUsername());

        User user3 = userMapper.selectById(3);
        Assertions.assertNull(user3);
    }
}

