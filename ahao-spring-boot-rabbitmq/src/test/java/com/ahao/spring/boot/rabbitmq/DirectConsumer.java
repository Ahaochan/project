package com.ahao.spring.boot.rabbitmq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DirectConsumer {
    public static final String QUEUE_NAME = "ahao_test";

    public static Object value;

    /**
     * 直接消费队列的消费者
     */
    @RabbitListener(queuesToDeclare = @Queue(QUEUE_NAME))
    @RabbitHandler
    public void directQueue(String msg) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收时间:"+sdf.format(new Date()));
        System.out.println("接收到的消息:"+msg);
        Thread.sleep(1000);
        value = msg;
    }
}
