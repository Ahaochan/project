package moe.ahao.spring.cloud.stream.rocketmq.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

@EnableBinding({Processor.class})
public class BaseTest {
    public static CountDownLatch latch;
    @Autowired
    protected Processor processor;

    @Autowired
    protected TestConfig.AhaoStreamListener ahaoStreamListener;

    @BeforeEach
    public void beforeEach() {
        ahaoStreamListener.result.clear();
    }

    protected void test(int size) throws Exception {
        latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            Message<String> message = MessageBuilder.withPayload("payload-" + i).build();
            processor.output().send(message);
        }

        boolean success = latch.await(60, TimeUnit.SECONDS);
        Assertions.assertTrue(success);

        ahaoStreamListener.result.sort(Comparator.comparing(String::toString));
        for (int i = 0; i < size; i++) {
            Assertions.assertEquals("payload-" + i, ahaoStreamListener.result.get(i));
        }
    }
}
