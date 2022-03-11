package moe.ahao.spring.boot.bytetcc.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static moe.ahao.spring.boot.bytetcc.consumer.TransferServiceConfirm.BEAN_NAME;

@Service(BEAN_NAME)
public class TransferServiceConfirm implements TransferService {
    public static final String BEAN_NAME = "transferServiceConfirm";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void transfer(String sourceAccountId, String targetAccountId, double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account2 set amount = amount + ?, frozen = frozen - ? where account_id = ? and forzen >= ?",
            amount, amount, targetAccountId, amount);
        String msg = "TCC事务: 转账confirm阶段: " + targetAccountId + "余额增加" + amount + ", 冻结减少" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }

}
