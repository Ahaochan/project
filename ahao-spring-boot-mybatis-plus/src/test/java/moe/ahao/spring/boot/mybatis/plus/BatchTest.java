package moe.ahao.spring.boot.mybatis.plus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import moe.ahao.spring.boot.mybatis.plus.config.MultiMyBatisConfig;
import moe.ahao.spring.boot.mybatis.plus.config.MyBatisPlusConfig;
import moe.ahao.transaction.mybatis.entity.User;
import moe.ahao.transaction.mybatis.mapper.UserMapper;
import moe.ahao.transaction.mybatis.service.UserMybatisService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {UserMybatisService.class,
    MyBatisPlusConfig.class, MultiMyBatisConfig.class,
    DataSourceAutoConfiguration.class,
    MybatisPlusAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
@ActiveProfiles("mysql")
public class BatchTest {
    int batchSize = 10000;
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
