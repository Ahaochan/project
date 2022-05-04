package moe.ahao.spring.boot.transaction.xa;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import moe.ahao.transaction.AbstractTransactionTest;

import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class AtomikosXANativeTest extends AbstractTransactionTest {

    private AtomikosDataSourceBean createAtomikosDataSourceBean(String resourceName) {
        Properties p = new Properties();
        p.setProperty("url", url("ahaodb"));
        p.setProperty("user", "root");
        p.setProperty("password", "root");

        // 1、mysql官方提供的com.mysql.jdbc.jdbc2.optional.MysqlXADataSource
        // 2、阿里巴巴开源的druid连接池，对应的实现类为com.alibaba.druid.pool.xa.DruidXADataSource
        // 3、tomcat-jdbc连接池提供的org.apache.tomcat.jdbc.pool.XADataSource
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(resourceName);
        ds.setXaDataSourceClassName(MysqlXADataSource.class.getName());
        ds.setXaProperties(p);
        return ds;
    }

    @Override
    protected void test(boolean rollback) throws Exception {
        AtomikosDataSourceBean atomikosDS1 = createAtomikosDataSourceBean("resource1");
        AtomikosDataSourceBean atomikosDS2 = createAtomikosDataSourceBean("resource2");

        UserTransaction userTransaction = new UserTransactionImp();
        // 必须在getConnection之前开启事务, 否则事务回滚失效
        userTransaction.begin();

        try (Connection connection1 = atomikosDS1.getConnection();
             Connection connection2 = atomikosDS2.getConnection();
             PreparedStatement ps1 = connection1.prepareStatement("update user set username = 'admin1' where id = 1");
             PreparedStatement ps2 = connection2.prepareStatement("update user set username = 'admin2' where id = 2");) {


            // 执行db1上的sql
            ps1.executeUpdate();
            if (rollback) {
                userTransaction.rollback();
                return;
            }
            ps2.executeUpdate();

            // 两阶段提交
            userTransaction.commit();
        } catch (Throwable e) {
            userTransaction.rollback();
            e.printStackTrace();
        } finally {
            atomikosDS1.close();
            atomikosDS2.close();
        }
    }
}
