package moe.ahao.spring.boot.bytetcc.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static moe.ahao.spring.boot.bytetcc.consumer.TransferServiceCancel.BEAN_NAME;

@Service(BEAN_NAME)
public class TransferServiceCancel implements TransferService {
    public static final String BEAN_NAME = "transferServiceCancel";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void transfer(String sourceAccountId, String targetAccountId, double amount) {
        int value = this.jdbcTemplate.update(
            "update ahao_account2 set frozen = frozen - ? where account_id = ? and forzen >= ?",
            amount, amount, targetAccountId, amount);
        String msg = "TCC事务: 转账cancel阶段: " + targetAccountId + "冻结减少" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }

}
