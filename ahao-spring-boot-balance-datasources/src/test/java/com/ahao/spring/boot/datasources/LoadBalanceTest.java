package com.ahao.spring.boot.datasources;

import com.ahao.spring.boot.datasources.config.DataSourceConfig;
import com.ahao.util.spring.SpringContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {SpringContextHolder.class, DataSourceConfig.class})
@EnableAutoConfiguration
@ActiveProfiles("test")
class LoadBalanceTest {
    @Autowired
    @Qualifier("dynamicDataSource")
    private DataSource dataSource;

    @Test
    @DisplayName("测试主库写入")
    void master() {
        DataSourceContextHolder.set("master");
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {

            Assertions.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            Assertions.assertAll("查出的数据有误",
                () -> Assertions.assertEquals(1, id),
                () -> Assertions.assertEquals("db1", name)
            );
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("测试从库轮询读取")
    void slave() {
        DataSourceContextHolder.set("slave");
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {

            Assertions.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            Assertions.assertAll("查出的数据有误",
                () -> Assertions.assertEquals(1, id),
                () -> Assertions.assertEquals("db3", name)
            );
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {

            Assertions.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            Assertions.assertAll("查出的数据有误",
                () -> Assertions.assertEquals(1, id),
                () -> Assertions.assertEquals("db2", name)
            );
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
