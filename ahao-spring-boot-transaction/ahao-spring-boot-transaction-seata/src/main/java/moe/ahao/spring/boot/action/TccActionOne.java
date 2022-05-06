package moe.ahao.spring.boot.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface TccActionOne {
    @TwoPhaseBusinessAction(name = "TccActionOne", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepare(BusinessActionContext actionContext, int a);
    boolean commit(BusinessActionContext actionContext);
    boolean rollback(BusinessActionContext actionContext);
}
