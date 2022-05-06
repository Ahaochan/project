package moe.ahao.spring.boot.transaction.seata.tcc;

import io.seata.common.util.StringUtils;
import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.action.ResultHolder;
import moe.ahao.spring.boot.service.TccTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class LocalTccTransactionStarter {
    @Autowired
    public TccTransactionService tccTransactionService;

    @Test
    public void test() throws Exception {
        //分布式事务提交demo
        transactionCommitDemo();

        //分布式事务回滚demo
        transactionRollbackDemo();
    }

    private void transactionCommitDemo() throws InterruptedException {
        String txId = tccTransactionService.doTransactionCommit();
        System.out.println(txId);
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");

        Thread.sleep(1000L);

        Assert.isTrue("T".equals(ResultHolder.getActionOneResult(txId)), "tccActionOne commit failed");
        Assert.isTrue("T".equals(ResultHolder.getActionTwoResult(txId)), "tccActionTwo commit failed");

        System.out.println("transaction commit demo finish.");
    }

    private void transactionRollbackDemo() throws InterruptedException {
        Map map = new HashMap(16);
        try {
            tccTransactionService.doTransactionRollback(map);
            Assert.isTrue(false, "分布式事务未回滚");
        } catch (Throwable t) {
            Assert.isTrue(true, "分布式事务异常回滚");
        }
        String txId = (String)map.get("xid");
        Thread.sleep(1000L);

        Assert.isTrue("R".equals(ResultHolder.getActionOneResult(txId)), "tccActionOne commit failed");
        Assert.isTrue("R".equals(ResultHolder.getActionTwoResult(txId)), "tccActionTwo commit failed");

        System.out.println("transaction rollback demo finish.");
    }

}
