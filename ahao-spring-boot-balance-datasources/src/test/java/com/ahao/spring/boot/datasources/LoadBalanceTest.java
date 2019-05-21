package com.ahao.spring.boot.datasources;

import com.ahao.spring.boot.datasources.datasource.DataSourceConfiguration;
import com.ahao.spring.boot.datasources.datasource.LoadBalanceConfiguration;
import com.ahao.util.spring.SpringContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ContextConfiguration(classes = {DataSourceConfiguration.class, LoadBalanceConfiguration.class, SpringContextHolder.class})
@EnableAutoConfiguration
@ActiveProfiles("test-db")
public class LoadBalanceTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDatasource;

    @Autowired
    @Qualifier("slave1DataSource")
    private DataSource slave1DataSource;

    @Autowired
    @Qualifier("slave2DataSource")
    private DataSource slave2DataSource;

    @Test
    public void master() {
        DataSourceContextHolder.master();
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
        DataSourceContextHolder.slave();
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
    }
}
