package moe.ahao.spring.boot.bytetcc.consumer;

import org.bytesoft.compensable.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Compensable(interfaceClass = TransferService.class, cancellableKey = TransferServiceCancel.BEAN_NAME)
@RestController
public class TransferTryController implements TransferService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @GetMapping("/transfer")
    @Override
    public void transfer(@RequestParam String sourceAccountId, @RequestParam String targetAccountId, @RequestParam double amount) {
        this.restTemplate.getForEntity(String.format("http://127.0.0.1:8080/decrease?accountId=%s&amount=%s", sourceAccountId, amount), null, Void.TYPE);
        int value = this.jdbcTemplate.update("update ahao_account2 set amount = amount + ? where account_id = ?", amount, targetAccountId);
        String msg = "TCC事务: 转账try阶段: " + targetAccountId + "余额增加" + amount;
        if (value != 1) {
            throw new IllegalStateException(msg + ", 失败");
        }
        System.out.println(msg);
    }
}
