package moe.ahao.spring.boot.transaction.seata.tcc.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface OutTransferService {
    @TwoPhaseBusinessAction(name = "OutTransferService", commitMethod = "confirm", rollbackMethod = "cancel")
    void prepare(BusinessActionContext actionContext,
                 // 这里会强制用FastJson序列化, 所以最好自己序列化成String
                 @BusinessActionContextParameter(paramName = "json") String json) throws Exception;
    void confirm(BusinessActionContext actionContext) throws Exception;
    void cancel(BusinessActionContext actionContext) throws Exception;
}
