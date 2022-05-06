package moe.ahao.spring.boot.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import moe.ahao.spring.boot.action.ResultHolder;
import moe.ahao.spring.boot.action.TccActionTwo;
import org.springframework.stereotype.Component;

@Component
public class TccActionTwoImpl implements TccActionTwo {

    @Override
    public boolean prepare(BusinessActionContext actionContext, String b) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + xid);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo commit, xid:" + xid);
        ResultHolder.setActionTwoResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo rollback, xid:" + xid);
        ResultHolder.setActionTwoResult(xid, "R");
        return true;
    }

}
