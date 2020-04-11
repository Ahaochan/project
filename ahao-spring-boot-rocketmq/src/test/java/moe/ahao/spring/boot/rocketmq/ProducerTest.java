package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RocketMQAutoConfiguration.class, MyRocketMQListener.class})
@ActiveProfiles("rocketmq")
public class ProducerTest {
    public static final String TOPIC  = "ahao-topic";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private MyRocketMQListener listener;

    @Test
    public void simpleMessage() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.convertAndSend(TOPIC, "payload-"+i);
        }
        Thread.sleep(5000);

        listener.receiveList.sort(Comparator.comparing(String::toString));
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals("payload-"+i, listener.receiveList.get(i));
        }
    }
}
