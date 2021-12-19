package moe.ahao.spring.boot.transaction.xa;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.transaction.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static moe.ahao.spring.boot.atomikos.AtomikosConfig.TX_MANAGER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {Starter.class, AtomikosXATest.AhaoService.class})

@ActiveProfiles("atomikos")
public class AtomikosXATest extends BaseTest {

    @Transactional(transactionManager = TX_MANAGER, rollbackFor = Exception.class)
    public static class AhaoService {
        @Autowired
        private JdbcTemplate jdbcTemplate;

        public void update(int i) {
            jdbcTemplate.update("update user set username = 'admin" + i + "' where id = " + i);
        }
    }

    @Autowired
    private AhaoService ahaoService;
    @Override
    protected void test(boolean rollback) throws Exception {
        ahaoService.update(1);
        if(rollback) {
            int i = 1/0; // TODO 回滚失败
        }
        ahaoService.update(2);
    }
}
