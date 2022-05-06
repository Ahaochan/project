package moe.ahao.spring.boot.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import moe.ahao.spring.boot.action.ResultHolder;
import moe.ahao.spring.boot.action.TccActionOne;
import org.springframework.stereotype.Component;


@Component
public class TccActionOneImpl implements TccActionOne {

    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne prepare, xid:" + xid);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne commit, xid:" + xid);
        ResultHolder.setActionOneResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne rollback, xid:" + xid);
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}
