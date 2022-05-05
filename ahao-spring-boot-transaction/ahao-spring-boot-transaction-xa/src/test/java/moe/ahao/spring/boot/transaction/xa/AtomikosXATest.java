package moe.ahao.spring.boot.transaction.xa;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import moe.ahao.spring.boot.atomikos.AtomikosConfig;
import moe.ahao.transaction.AbstractTransactionTest;
import moe.ahao.transaction.bank.transfer.service.BankTransferAccountMybatisService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static moe.ahao.spring.boot.atomikos.AtomikosConfig.TX_MANAGER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {AtomikosConfig.class, AtomikosXATest.AhaoService.class, BankTransferAccountMybatisService.class,
    TransactionAutoConfiguration.class,
    MybatisPlusAutoConfiguration.class})
@ActiveProfiles("atomikos")
public class AtomikosXATest extends AbstractTransactionTest {
    @Autowired
    private AhaoService ahaoService;
    @Override
    protected void execute(boolean rollback) throws Exception {
        ahaoService.update(rollback);
    }

    @Transactional(transactionManager = TX_MANAGER, rollbackFor = Exception.class)
    @MapperScan(value = "moe.ahao.**.mapper")
    public static class AhaoService {
        @Autowired
        private BankTransferAccountMybatisService service;

        public void update(boolean rollback) {
            service.decrease(1L, new BigDecimal("100"));
            if (rollback) {
                int i = 1 / 0;
            }
            service.increase(2L, new BigDecimal("100"));
        }
    }
}
