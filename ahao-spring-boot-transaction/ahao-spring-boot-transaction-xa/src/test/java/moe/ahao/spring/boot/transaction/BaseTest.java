package moe.ahao.spring.boot.transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class BaseTest {
    public static final String URL = "jdbc:mysql://localhost:3306";
    public static final String SQL1 = "update user set username = 'admin1' where id = 1";
    public static final String SQL2 = "update user set username = 'admin2' where id = 2";

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, "root", "root");
             Statement s = conn.createStatement();) {
            s.execute("create database if not exists ahaodb");
            s.execute("drop table if exists ahaodb.user");
            s.execute("create table ahaodb.user ( id int primary key, username varchar(50))");
            s.execute("insert ahaodb.user (id, username) values (1, 'user1'), (2, 'user2')");
        }
    }

    @Test
    public void commit() throws Exception {
        try {
            test(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertCommit();
    }

    @Test
    public void rollback() throws Exception {
        try {
            test(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertRollback();
    }

    @AfterEach
    public void afterEach() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, "root", "root");
             Statement s = conn.createStatement();) {
            s.execute("drop table if exists ahaodb.user");
            s.execute("drop database if exists ahaodb");
        }
    }

    protected abstract void test(boolean rollback) throws Exception;

    public void assertCommit() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, "root", "root");
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery("select username from ahaodb.user");) {

            rs.next();
            Assertions.assertEquals("admin1", rs.getString("username"));
            rs.next();
            Assertions.assertEquals("admin2", rs.getString("username"));
        }
    }

    public void assertRollback() throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, "root", "root");
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery("select username from ahaodb.user");) {

            rs.next();
            Assertions.assertEquals("user1", rs.getString("username"));
            rs.next();
            Assertions.assertEquals("user2", rs.getString("username"));
        }
    }
}
