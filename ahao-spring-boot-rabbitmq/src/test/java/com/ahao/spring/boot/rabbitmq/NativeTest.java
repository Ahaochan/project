package com.ahao.spring.boot.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NativeTest {
    public static final String EXCHANGE_NAME = "ahao-exchange";
    public static final String ROUTING_KEY = "ahao-routing-key";
    public static final String QUEUE_NAME = "ahao-queue";

    @Test
    public void producer() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            // 3. 创建 Exchange 和 Queue 并绑定
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);

            // 4. 发送消息
            String msg = "现在时间"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void consumer() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            // 3. 创建 Exchange 和 Queue 并绑定
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);

            // 4. 消费消息
            CountDownLatch latch = new CountDownLatch(1);
            channel.basicQos(64);
            channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("接受到:" + new String(body));
                    latch.countDown();
                }
            });

            // 5. 等待消息消费后, 再关闭资源
            latch.await(10, TimeUnit.SECONDS);
        }
    }

    public ConnectionFactory initFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.8.60.56");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }
}
