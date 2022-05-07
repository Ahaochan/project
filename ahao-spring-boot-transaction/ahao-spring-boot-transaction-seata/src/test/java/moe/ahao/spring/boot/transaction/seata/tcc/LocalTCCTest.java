package moe.ahao.spring.boot.transaction.seata.tcc;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.transaction.seata.tcc.service.TccGlobalService;
import moe.ahao.transaction.AbstractTransactionTest;
import moe.ahao.transaction.bank.transfer.service.BankTransferAccountMybatisService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@MapperScan("moe.ahao.**.mapper")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {Starter.class,
    BankTransferAccountMybatisService.class})
public class LocalTCCTest extends AbstractTransactionTest {
    @Autowired
    public TccGlobalService tccGlobalService;

    @Override
    protected void doCommit() throws Exception {
        tccGlobalService.transfer(1L, 2L, new BigDecimal("100"));
        Thread.sleep(1000L);
    }

    @Override
    protected void doRollback() throws Exception {
        tccGlobalService.transfer(1L, 2L, new BigDecimal("200"));
        Thread.sleep(1000L);
    }
}
