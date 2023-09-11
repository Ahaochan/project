package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Comparator;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RocketMQAutoConfiguration.class,
    MyRocketMQListener.class, MyRocketMQTransactionListener.class})
@ActiveProfiles("rocketmq")
public class RocketMQTemplateTest {
    public static final String TOPIC  = Constant.DEFAULT_TOPIC;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private MyRocketMQListener listener;

    @Test
    public void simpleMessage() throws Exception {
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.convertAndSend(TOPIC, "payload-"+i);
        }
        Thread.sleep(5000);

        listener.receiveList.sort(Comparator.comparing(String::toString));
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals("payload-"+i, listener.receiveList.get(i));
        }
    }

    @Test
    public void transactionMessage() throws Exception {
        for (int i = 0; i < 10; i++) {
            Message message = MessageBuilder.withPayload("payload-"+i)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, UUID.randomUUID().toString())
                .build();
            this.rocketMQTemplate.sendMessageInTransaction(TOPIC, message, null);
        }
        Thread.sleep(5000);

        listener.receiveList.sort(Comparator.comparing(String::toString));
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals("payload-"+i, listener.receiveList.get(i));
        }
    }

    @AfterEach
    public void after() {
        listener.receiveList.clear();
    }
}
