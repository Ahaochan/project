package moe.ahao.spring.boot.datasources;

import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test")
// TODO No qualifying bean of type 'javax.sql.DataSource' available: expected at least 1 bean which qualifies as autowire candidate.
// @ContextConfiguration(classes = {SpringContextHolder.class, DataSourceConfig.class, DataSourcePropertiesMemoryImpl.class,
//     ConfigurationPropertiesAutoConfiguration.class})
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
