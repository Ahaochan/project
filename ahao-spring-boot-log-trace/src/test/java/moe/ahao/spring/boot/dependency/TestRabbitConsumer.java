package moe.ahao.spring.boot.dependency;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RabbitListener(queuesToDeclare = @Queue(TestRabbitConsumer.QUEUE_NAME))
public class TestRabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TestRabbitConsumer.class);
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static final String QUEUE_NAME = "ahao_test";

    public static Object value;

    @RabbitHandler
    public void directQueue(@Payload String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.debug("消息接收时间:" + sdf.format(new Date()));
            logger.debug("接收到的消息:" + msg);
            logger.trace("Rabbit trace");
            logger.debug("Rabbit debug");
            logger.info("Rabbit info");
            logger.warn("Rabbit warn");
            logger.error("Rabbit error");
            Thread.sleep(1000);
            value = msg;

            latch.countDown();

            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        }
    }
}

