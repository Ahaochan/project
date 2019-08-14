package com.ahao.spring.boot.rabbitmq;

import com.ahao.util.spring.SpringContextHolder;
import com.ahao.util.spring.mq.RabbitMQHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitMQConfig.class, RabbitAutoConfiguration.class, SpringContextHolder.class, DirectConsumer.class})
public class DirectProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectConsumer consumer;

    @BeforeEach
    public void before() {
        Assertions.assertNotNull(rabbitTemplate);
        Assertions.assertNotNull(consumer);
        DirectConsumer.value = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息发送时间:" + sdf.format(new Date()));
    }

    @Test
    public void send() throws Exception {
        String msg = "send()";
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelay() throws Exception {
        String msg = "sendDelay()";
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 3000);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(10000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelayError() throws Exception {
        String msg = "sendDelayError()";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 0xffffffffL + 1);
        });
    }
}
