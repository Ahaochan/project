package moe.ahao.spring.boot.mybatis.plus;

import moe.ahao.transaction.DBTestUtils;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class NativeTest {
    @MapperScan("moe.ahao.spring.boot.mybatis.plus")
    static class TestConfig {
        @Bean
        public DataSource dataSource(){
            return DBTestUtils.createH2DataSource();
        }
        @Bean
        public SqlSessionFactory sqlSessionFactory() throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource());
            return factoryBean.getObject();
        }
    }

    interface AhaoMapper {
        @Select("select * from t")
        List<Map<String, Object>> findList();
        @Select("select * from t where id = ${id}")
        Map<String, Object> findOne(Integer id);
    }

    @Test
    public void spring() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

        DataSource ds = context.getBean(DataSource.class);
        try (Connection connection = ds.getConnection();
        Statement statement = connection.createStatement();) {
            // TODO 使用通用的实体类
            statement.executeUpdate("create table if not exists t(id int primary key , name varchar(50));");
            statement.executeUpdate("insert into t(id, name) values(1, 'admin')");
        }

        AhaoMapper mapper = context.getBean(AhaoMapper.class);
        List<Map<String, Object>> list = mapper.findList();
        Map<String, Object> one = mapper.findOne(1);

        Assertions.assertEquals(one, list.get(0));

        DBTestUtils.clearDB(ds);
    }

    @Test
    public void mybatis() throws Exception {
        TestConfig config = new TestConfig();
        DataSource ds = config.dataSource();
        Configuration configuration = new Configuration(new Environment("development", new JdbcTransactionFactory(), ds));
        configuration.addMapper(AhaoMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement();) {
            // TODO 使用通用的实体类
            statement.executeUpdate("create table if not exists t(id int primary key , name varchar(50));");
            statement.executeUpdate("insert into t(id, name) values(1, 'admin')");
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
            AhaoMapper mapper = sqlSession.getMapper(AhaoMapper.class);
            List<Map<String, Object>> list = mapper.findList();
            Map<String, Object> one = mapper.findOne(1);

            Assertions.assertEquals(one, list.get(0));
        }

        DBTestUtils.clearDB(ds);
    }
}
