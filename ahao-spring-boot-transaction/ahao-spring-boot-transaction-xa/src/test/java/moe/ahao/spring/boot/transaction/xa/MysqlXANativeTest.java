package moe.ahao.spring.boot.transaction.xa;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;
import moe.ahao.spring.boot.transaction.BaseTest;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlXANativeTest extends BaseTest {

    public void test(boolean rollback) throws Exception {
        // 这个XAResource其实你可以认为是RM（Resource Manager）的一个代码中的对象实例
        XAConnection xaConnection1 = null;
        XAConnection xaConnection2 = null;
        try (Connection connection1 = DriverManager.getConnection(URL + "/ahaodb", "root", "root");
             Connection connection2 = DriverManager.getConnection(URL + "/ahaodb", "root", "root");) {
            // 这个XAResource其实你可以认为是RM（Resource Manager）的一个代码中的对象实例
            xaConnection1 = new MysqlXAConnection((JdbcConnection) connection1, true);
            XAResource xaResource1 = xaConnection1.getXAResource();
            // 这个XAResource其实你可以认为是RM（Resource Manager）的一个代码中的对象实例
            xaConnection2 = new MysqlXAConnection((JdbcConnection) connection2, true);
            XAResource xaResource2 = xaConnection2.getXAResource();

            // 定义子事务要执行的SQL语句, xid是子事务的唯一标识
            Xid xid1 = this.prepare(xaResource1, "bqual1", connection1.prepareStatement(SQL1));
            Xid xid2 = this.prepare(xaResource2, "bqual2", connection2.prepareStatement(SQL2));
            // 2PC的阶段一: 向两个库都发送prepare消息，执行事务中的SQL语句，但是不提交
            int prepareResult1 = xaResource1.prepare(xid1);
            int prepareResult2 = xaResource2.prepare(xid2);

            if(rollback) {
                xaResource1.rollback(xid1);
                xaResource2.rollback(xid2);
                return;
            }

            // 2PC的阶段二: 两个库都发送commit消息，提交事务
            // 如果两个库对prepare都返回ok, 那么就全部commit, 对每个库都发送commit消息, 完成自己本地事务的提交
            if (prepareResult1 == XAResource.XA_OK && prepareResult2 == XAResource.XA_OK) {
                xaResource1.commit(xid1, false);
                xaResource2.commit(xid2, false);
            }
            // 如果不是所有库都对prepare返回ok，那么就全部rollback
            else {
                xaResource1.rollback(xid1);
                xaResource2.rollback(xid2);
            }
        } finally {
            if (xaConnection1 != null) xaConnection1.close();
            if (xaConnection2 != null) xaConnection2.close();
        }
    }

    private Xid prepare(XAResource xaResource, String bqualStr, PreparedStatement statement) throws Exception {
        // gtrid: 全局事务ID
        // bqual: 分支修饰词标识符(branch qualifier)
        // formatID: 是一个无符号整数，用于标记gtrid和bqual值的格式，默认为1，长度不超过64.
        byte[] gtrid = "g12345".getBytes();
        byte[] bqual = bqualStr.getBytes();
        int formatId = 1;
        // 这个xid代表了一个子事务
        Xid xid = new MysqlXid(gtrid, bqual, formatId);

        // 通过START和END两个操作, 定义子事务要执行的SQL语句
        // 但是这里的SQL绝对不会执行的, 只是说先定义好, 在子事务中要执行哪些SQL
        xaResource.start(xid, XAResource.TMNOFLAGS);
        statement.execute();
        xaResource.end(xid, XAResource.TMSUCCESS);
        return xid;
    }
}
