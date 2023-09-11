package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.ArrayList;
import java.util.List;

@RocketMQTransactionListener
public class MyRocketMQTransactionListener implements RocketMQLocalTransactionListener {
    public List<String> receiveList = new ArrayList<>();

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        MessageHeaders headers = msg.getHeaders();
        String transactionId = headers.get(RocketMQHeaders.TRANSACTION_ID, String.class);
        String payload = msg.getPayload().toString();

        try {
            if(receiveList.size() > 5) {
                return RocketMQLocalTransactionState.UNKNOWN; // 模拟异常
            }
            receiveList.add(payload);
            System.out.println("本地事务消息 transactionId:" + transactionId + ", payload:" + payload);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        MessageHeaders headers = msg.getHeaders();
        String transactionId = headers.get(RocketMQHeaders.TRANSACTION_ID, String.class);
        String payload = msg.getPayload().toString();

        try {
            System.out.println("本地事务消息, 异常回查: transactionId:" + transactionId + ", payload:" + payload);
            if(receiveList.contains(payload)) {
                return RocketMQLocalTransactionState.COMMIT;
            } else {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
