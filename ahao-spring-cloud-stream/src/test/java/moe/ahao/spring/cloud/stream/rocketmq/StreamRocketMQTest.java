package moe.ahao.spring.cloud.stream.rocketmq;

import moe.ahao.spring.cloud.Starter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {Starter.class, StreamRocketMQTest.AhaoStreamListener.class})
@ActiveProfiles("rocketmq")

@EnableBinding({Processor.class})
public class StreamRocketMQTest {
    @Autowired
    private Processor processor;

    @Test
    public void test1() throws Exception {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("payload-" + i).build();
            processor.output().send(message);
        }
        Thread.sleep(5000);

        AhaoStreamListener.result.sort(Comparator.comparing(String::toString));
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals("payload-"+i, AhaoStreamListener.result.get(i));
        }

    }

    @AfterEach
    public void afterEach() {
        AhaoStreamListener.result.clear();
    }

    public static class AhaoStreamListener {
        public static final List<String> result = new ArrayList<>();

        @StreamListener(Processor.INPUT)
        public void receive(String message) {
            System.out.println("接收到:" + message);
            result.add(message);
        }
    }
}
