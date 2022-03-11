package moe.ahao.spring.boot.bytetcc.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static moe.ahao.spring.boot.bytetcc.provider.AccountServiceCancel.BEAN_NAME;

@Service(BEAN_NAME)
public class AccountServiceCancel implements AccountService {
    public static final String BEAN_NAME = "accountServiceCancel";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void increaseAmount(String accountId, double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account1 set frozen = frozen - ? where account_id = ? and frozen >= ?;",
            amount, accountId, amount);
        String msg = "增加金额cancel阶段: " + accountId + "冻结减少" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }

    @Transactional
    @Override
    public void decreaseAmount(String accountId, double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account1 set amount = amount + ?, frozen = frozen - ? where account_id = ? and frozen >= ?;",
            amount, amount, accountId, amount);
        String msg = "TCC事务: 减少金额cancel阶段: " + accountId + "余额增加" + amount + ", 冻结减少" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }
}
