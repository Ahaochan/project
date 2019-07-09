package com.ahao.spring.boot.rabbitmq.consumer;

import com.ahao.spring.boot.rabbitmq.util.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);


    @RabbitListener(queuesToDeclare = @Queue("queue1"))
    public void myQueue1(String msg) throws Exception {
        Thread.sleep(1000);
        ObjectPool.put("queue1", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange("exchange2"),
        key = "routingKey21",
        value = @Queue("queue21")))
    public void myQueue21(String msg) throws Exception {
        Thread.sleep(1000);
        ObjectPool.put("queue21", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange("exchange2"),
        key = "routingKey22",
        value = @Queue("queue22")))
    public void myQueue22(String msg) throws Exception {
        Thread.sleep(1000);
        ObjectPool.put("queue22", msg);
    }
}
