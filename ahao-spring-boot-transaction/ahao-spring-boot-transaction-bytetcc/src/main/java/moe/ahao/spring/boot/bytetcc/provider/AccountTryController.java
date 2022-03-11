package moe.ahao.spring.boot.bytetcc.provider;

import org.bytesoft.compensable.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务提供方Controller必须添加@Compensable注解
 * 这里是Try阶段的逻辑
 */
@Compensable(interfaceClass = AccountService.class, confirmableKey = AccountServiceConfirm.BEAN_NAME, cancellableKey = AccountServiceCancel.BEAN_NAME)
@RestController
public class AccountTryController implements AccountService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/increase")
    @Transactional
    @Override
    public void increaseAmount(@RequestParam String accountId, @RequestParam double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account1 set frozen = frozen + ? where account_id = ?;",
            amount, accountId);
        String msg = "TCC事务: 增加金额try阶段: " + accountId + "冻结增加" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }

    @GetMapping("/decrease")
    @Transactional
    @Override
    public void decreaseAmount(@RequestParam String accountId, @RequestParam double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account1 set amount = amount - ?, frozen = frozen + ? where account_id = ? and amount >= ?;",
            amount, amount, accountId, amount);
        String msg = "TCC事务: 减少金额try阶段: " + accountId + "余额扣减" + amount + ", 并冻结" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }
}
