package moe.ahao.spring.boot.service;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import moe.ahao.spring.boot.action.TccActionOne;
import moe.ahao.spring.boot.action.TccActionTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TccTransactionService {
    @Autowired
    private TccActionOne tccActionOne;
    @Autowired
    private TccActionTwo tccActionTwo;

    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result = tccActionTwo.prepare(null, "two");
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    @GlobalTransactional
    public String doTransactionRollback(Map map) {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result = tccActionTwo.prepare(null, "two");
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());
        throw new RuntimeException("transacton rollback");
    }
}
