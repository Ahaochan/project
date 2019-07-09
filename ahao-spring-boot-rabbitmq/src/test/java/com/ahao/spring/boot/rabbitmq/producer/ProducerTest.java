package com.ahao.spring.boot.rabbitmq.producer;

import com.ahao.spring.boot.Starter;
import com.ahao.spring.boot.rabbitmq.util.ObjectPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
class ProducerTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    void direct2Queue() throws Exception {
        String msg = "message1";
        amqpTemplate.convertAndSend("queue1", msg);
        Assertions.assertNull(ObjectPool.get("queue1"));

        Thread.sleep(5000);
        Assertions.assertEquals(msg, ObjectPool.get("queue1"));
    }

    @Test
    void routingKey() throws Exception {
        String msg1 = "message1";
        amqpTemplate.convertAndSend("exchange2", "routingKey21", msg1);
        Assertions.assertNull(ObjectPool.get("queue21"));
        Thread.sleep(5000);
        Assertions.assertEquals(msg1, ObjectPool.get("queue21"));

        String msg2 = "message2";
        amqpTemplate.convertAndSend("exchange2", "routingKey22", msg2);
        Assertions.assertNull(ObjectPool.get("queue22"));
        Thread.sleep(5000);
        Assertions.assertEquals(msg2, ObjectPool.get("queue22"));
    }
}
