package moe.ahao.spring.boot.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface TccActionTwo {
    @TwoPhaseBusinessAction(name = "TccActionTwo", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepare(BusinessActionContext actionContext, String b);
    boolean commit(BusinessActionContext actionContext);
    boolean rollback(BusinessActionContext actionContext);
}
