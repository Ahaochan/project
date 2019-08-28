package com.ahao.spring.boot.rabbitmq;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.domain.entity.BaseDO;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 直接消费队列的消费者
 */
@Service
@RabbitListener(queuesToDeclare = @Queue(DirectConsumer.QUEUE_NAME))
public class DirectConsumer {
    public static final String QUEUE_NAME = "ahao_test";

    public static Object value;


    @RabbitHandler
    public void directQueue(@Payload String msg) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收时间:"+sdf.format(new Date()));
        System.out.println("接收到的消息:"+msg);
        Thread.sleep(1000);
        value = msg;
    }

    @RabbitHandler
    public void directQueue(@Payload Integer msg) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收时间:"+sdf.format(new Date()));
        System.out.println("接收到的消息:"+msg);
        Thread.sleep(1000);
        value = msg;
    }

    @RabbitHandler
    public void directQueue(@Payload AjaxDTO msg) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收时间:"+sdf.format(new Date()));
        System.out.println("接收到的消息:"+msg.getResult() +"," + msg.getMsg() +"," +msg.getObj());
        Thread.sleep(1000);
        value = msg;
    }

    @RabbitHandler
    public void directQueue(@Payload BaseDO msg) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收时间:"+sdf.format(new Date()));
        System.out.println("接收到的消息:"+msg.toString());
        Thread.sleep(1000);
        value = msg;
    }
}
