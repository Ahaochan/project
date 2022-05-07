package moe.ahao.spring.boot.transaction.seata.tcc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.seata.rm.tcc.api.BusinessActionContext;
import moe.ahao.spring.boot.seata.tcc.TccResultHolder;
import moe.ahao.spring.boot.transaction.seata.tcc.service.InTransferService;
import moe.ahao.transaction.bank.transfer.dto.TransferDTO;
import moe.ahao.transaction.bank.transfer.service.BankTransferAccountMybatisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InTransferServiceImpl implements InTransferService {
    private static final Logger logger = LoggerFactory.getLogger(OutTransferServiceImpl.class);

    @Autowired
    private BankTransferAccountMybatisService bankTransferAccountMybatisService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void prepare(BusinessActionContext actionContext, String json) throws Exception {
        String xid = actionContext.getXid();
        TransferDTO transferDTO = objectMapper.readValue(json, TransferDTO.class);
        String transferNo = transferDTO.getTransferNo();
        Long accountId = transferDTO.getAccountId();
        BigDecimal amount = transferDTO.getAmount();

        // 标识try阶段开始执行
        logger.info("prepare阶段标记start, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        TccResultHolder.tagTryStart(getClass(), transferNo, xid);

        // 悬挂问题: rollback接口先进行了空回滚, try接口才执行, 导致try接口预留的资源无法被confirm和cancel
        // 解决方案: 当出现空回滚时, 在数据库中插一条记录, 在try这里判断一下
        if (this.isEmptyRollback()) {
            logger.info("prepare阶段出现悬挂, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
            throw new IllegalStateException("出现悬挂");
        }

        logger.info("prepare阶段进行, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        bankTransferAccountMybatisService.increasePrepare(accountId, amount);

        // 标识try阶段执行成功
        logger.info("prepare阶段标记success, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        TccResultHolder.tagTrySuccess(getClass(), transferNo, xid);
    }

    @Override
    public void confirm(BusinessActionContext actionContext) throws Exception {
        String xid = actionContext.getXid();
        String json = (String) actionContext.getActionContext("json");
        TransferDTO transferDTO = objectMapper.readValue(json, TransferDTO.class);
        String transferNo = transferDTO.getTransferNo();
        Long accountId = transferDTO.getAccountId();
        BigDecimal amount = transferDTO.getAmount();
        logger.info("confirm阶段开始, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);

        // 当出现网络异常或者TC Server异常时, 会出现重复调用commit阶段的情况, 所以需要进行幂等判断
        if (!TccResultHolder.isTrySuccess(getClass(), transferNo, xid)) {
            logger.info("confirm阶段幂等性校验失败, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
            return;
        }

        logger.info("confirm阶段进行, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        bankTransferAccountMybatisService.increaseConfirm(accountId, amount);

        //移除标识
        logger.info("confirm阶段结束, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        TccResultHolder.removeResult(getClass(), transferNo, xid);
    }

    @Override
    public void cancel(BusinessActionContext actionContext) throws Exception {
        String xid = actionContext.getXid();
        String json = (String) actionContext.getActionContext("json");
        TransferDTO transferDTO = objectMapper.readValue(json, TransferDTO.class);
        String transferNo = transferDTO.getTransferNo();
        Long accountId = transferDTO.getAccountId();
        BigDecimal amount = transferDTO.getAmount();
        logger.info("cancel阶段开始, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);

        // 空回滚处理
        if (TccResultHolder.isTagNull(getClass(), transferNo, xid)) {
            logger.info("cancel阶段发生空回滚, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
            insertEmptyRollbackTag();
            return;
        }

        // 幂等处理
        // try阶段没有完成的情况下，不必执行回滚，因为try阶段有本地事务，事务失败时已经进行了回滚
        // 如果try阶段成功，而其他全局事务参与者失败，这里会执行回滚
        if (!TccResultHolder.isTrySuccess(getClass(), transferNo, xid)) {
            logger.info("cancel阶段幂等性校验失败, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
            return;
        }

        logger.info("cancel阶段进行, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        bankTransferAccountMybatisService.increaseCancel(accountId, amount);

        // 移除标识
        logger.info("cancel阶段结束, 入账, 单号:{}, accountId:{}, amount:{}", transferNo, accountId, amount);
        TccResultHolder.removeResult(getClass(), transferNo, xid);
    }

    /**
     * 判断是否发生的空回滚
     */
    private Boolean isEmptyRollback() {
        // 需要查询本地数据库，看是否发生了空回滚
        return false;
    }

    /**
     * 插入空回滚标识
     */
    private void insertEmptyRollbackTag() {
        // 在数据库插入空回滚的标识
    }
}
