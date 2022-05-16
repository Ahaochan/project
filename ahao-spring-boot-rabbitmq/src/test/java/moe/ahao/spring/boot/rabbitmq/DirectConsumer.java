package moe.ahao.spring.boot.rabbitmq;

import com.rabbitmq.client.Channel;
import moe.ahao.domain.entity.BaseDO;
import moe.ahao.domain.entity.Result;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 直接消费队列的消费者
 */
@Service
@RabbitListener(queuesToDeclare = @Queue(DirectConsumer.QUEUE_NAME))
public class DirectConsumer {
    public static final String QUEUE_NAME = "ahao_test";

    public static CountDownLatch latch;
    public static Object value;

    @RabbitHandler
    public void directQueue(@Payload String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("消息接收时间:" + sdf.format(new Date()));
            System.out.println("接收到的消息:" + msg);
            Thread.sleep(1000);
            value = msg;

            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        } finally {
            latch.countDown();
        }
    }

    @RabbitHandler
    public void directQueue(@Payload Integer msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("消息接收时间:" + sdf.format(new Date()));
            System.out.println("接收到的消息:" + msg);
            Thread.sleep(1000);
            value = msg;

            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        } finally {
            latch.countDown();
        }
    }

    @RabbitHandler
    public void directQueue(@Payload Result<Object> msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("消息接收时间:" + sdf.format(new Date()));
            System.out.println("接收到的消息:" + msg.getCode() + "," + msg.getMsg() + "," + msg.getObj());
            Thread.sleep(1000);
            value = msg;

            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        } finally {
            latch.countDown();
        }
    }

    @RabbitHandler
    public void directQueue(@Payload BaseDO msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("消息接收时间:" + sdf.format(new Date()));
            System.out.println("接收到的消息:" + msg.toString());
            Thread.sleep(1000);
            value = msg;

            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        } finally {
            latch.countDown();
        }
    }
}
