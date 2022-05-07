package moe.ahao.spring.boot.transaction.xa;

import moe.ahao.spring.boot.Starter;
import moe.ahao.transaction.AbstractTransactionTest;
import moe.ahao.transaction.bank.transfer.service.BankTransferAccountMybatisService;
import moe.ahao.transaction.bank.transfer.service.BankTransferService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static moe.ahao.spring.boot.atomikos.AtomikosConfig.TX_MANAGER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = {Starter.class, AtomikosXATest.TestService.class, BankTransferService.class, BankTransferAccountMybatisService.class})
@ActiveProfiles("atomikos")

// @ContextConfiguration(classes = {AtomikosConfig.class, AtomikosXATest.AhaoService.class, BankTransferAccountMybatisService.class,
//     TransactionAutoConfiguration.class,
//     MybatisPlusAutoConfiguration.class})
public class AtomikosXATest extends AbstractTransactionTest {
    @Autowired
    private TestService testService;


    @Override
    protected void doCommit() throws Exception {
        testService.commit();
    }

    @Override
    protected void doRollback() throws Exception {
        testService.rollback();
    }

    @MapperScan(value = "moe.ahao.**.mapper")
    public static class TestService extends BankTransferService{
        public TestService(BankTransferAccountMybatisService bankTransferAccountMybatisService) {
            super(bankTransferAccountMybatisService);
        }

        @Transactional(transactionManager = TX_MANAGER, rollbackFor = Exception.class)
        public void commit() {
            this.transfer(1L, 2L, new BigDecimal("100"));
        }

        @Transactional(transactionManager = TX_MANAGER, rollbackFor = Exception.class)
        public void rollback() {
            this.transfer(1L, 2L, new BigDecimal("100"));
            this.transfer(1L, 2L, new BigDecimal("100")); // 没钱了所以回滚了
        }
    }
}
