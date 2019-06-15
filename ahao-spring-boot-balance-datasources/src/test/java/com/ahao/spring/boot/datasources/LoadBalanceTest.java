package com.ahao.spring.boot.datasources;

import com.ahao.spring.boot.datasources.config.DataSourceConfiguration;
import com.ahao.spring.boot.datasources.datasource.DynamicDataSource;
import com.ahao.util.spring.SpringContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {DataSourceConfiguration.class, DynamicDataSource.class, SpringContextHolder.class})
@EnableAutoConfiguration
@ActiveProfiles("test")
public class LoadBalanceTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void master() {
        DataSourceContextHolder.set("master");
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {


            Assert.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Assert.assertEquals(1, id);
            Assert.assertEquals("db1", name);

            Assert.assertFalse(resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void slave() {
        DataSourceContextHolder.set("slave");
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {


            Assert.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Assert.assertEquals(1, id);
            Assert.assertEquals("db3", name);

            Assert.assertFalse(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }


        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from sys_user where id = 1");) {


            Assert.assertTrue(resultSet.next());
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Assert.assertEquals(1, id);
            Assert.assertEquals("db2", name);

            Assert.assertFalse(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
