package moe.ahao.spring.boot.mybatis.plus;

import moe.ahao.spring.boot.Starter;
import moe.ahao.transaction.user.mybatis.entity.User;
import moe.ahao.transaction.user.mybatis.mapper.UserMapper;
import moe.ahao.transaction.user.mybatis.service.UserMybatisService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {Starter.class, UserMybatisService.class})
@ActiveProfiles("mysql")

// @ContextConfiguration(classes = {UserMybatisService.class,
//     MyBatisPlusConfig.class, MultiMyBatisConfig.class,
//     DataSourceAutoConfiguration.class,
//     MybatisPlusAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class BatchTest {
    public static final int batchSize = 100;
    @Autowired
    private UserMybatisService userMybatisService;
    @Autowired
    private UserMapper userMapper;

    private List<User> dataList;
    @BeforeEach
    public void beforeEach() {
        dataList = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            User user = new User();
            user.setUsername("username" + i);
            user.setPassword("password" + i);

            dataList.add(user);
        }
    }
    @AfterEach
    public void afterEach() {
        userMapper.truncate();
    }

    /**
     * MySQL的JDBC连接URL上要加上rewriteBatchedStatements=true参数
     */
    @Test @Order(5)
    void saveBatch() {
        userMybatisService.saveBatch(dataList);
    }

    @Test @Order(4)
    void insertBatchSQL() {
        userMapper.insertBatchSQL(dataList);
    }

    @Test @Order(3)
    void save() {
        for (User user : dataList) {
            userMybatisService.save(user);
        }
    }

    @Test @Order(2)
    void insert() {
        for (User user : dataList) {
            userMapper.insert(user);
        }
    }

    @Test @Order(1)
    void insertSQL() {
        for (User user : dataList) {
            userMapper.insertSQL(user);
        }
    }
}
