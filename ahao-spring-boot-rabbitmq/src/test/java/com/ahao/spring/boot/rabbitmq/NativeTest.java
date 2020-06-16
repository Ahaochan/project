package com.ahao.spring.boot.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

public class NativeTest {
    public static final String EXCHANGE_NAME = "ahao-exchange";
    public static final String ROUTING_KEY = "ahao-routing-key";
    public static final String QUEUE_NAME = "ahao-queue";

    @BeforeEach
    public void beforeEach() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();
        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            // 3. 创建 Exchange 和 Queue 并绑定
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);
        }
    }

    @Test
    public void producer() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            // 3. 发送消息
            String msg = "现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
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
            // 3. 消费消息
            CountDownLatch latch = new CountDownLatch(1);
            channel.basicQos(64);
            channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("接受到:" + new String(body));
                    latch.countDown();
                }
            });

            // 4. 等待消息消费后, 再关闭资源
            latch.await();
        }
    }

    @Test
    public void simpleConfirm() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            // 3. 开启确认模式
            int size = 10;
            channel.confirmSelect();

            // 4. 发送消息
            for (int i = 0; i < size; i++) {
                long deliveryTag = channel.getNextPublishSeqNo();
                String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
                if (!channel.waitForConfirms()) {
                    System.out.println("消息[" + msg + "]发送失败");
                }
            }
        }
    }

    @Test
    public void batchConfirm() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            // 3. 开启确认模式
            int size = 10;
            channel.confirmSelect();

            // 4. 发送消息
            for (int i = 0; i < size; i++) {
                long deliveryTag = channel.getNextPublishSeqNo();
                String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            }
            if (!channel.waitForConfirms()) {
                System.out.println("消息发送失败");
            }
        }
    }

    @Test
    public void asyncConfirm() throws Exception {
        // 1. 建立连接工厂
        ConnectionFactory factory = this.initFactory();

        // 2. 获取连接, 获取 Channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            // 3. 开启确认模式
            int size = 10;
            CountDownLatch latch = new CountDownLatch(size);
            TreeSet<Long> confirmSet = new TreeSet<>(); // 记录未确认的 deliveryTag

            channel.confirmSelect();
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    try {
                        if (multiple) {
                            System.out.println("消息唯一标识:" + deliveryTag + " 之前的消息都 ACK");
                            confirmSet.headSet(deliveryTag - 1).clear();
                        } else {
                            System.out.println("消息唯一标识:" + deliveryTag + " 消息 ACK");
                            confirmSet.remove(deliveryTag);
                        }
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    try {
                        if (multiple) {
                            System.out.println("消息唯一标识:" + deliveryTag + " 之前的消息都 NACK");
                            confirmSet.headSet(deliveryTag - 1).clear();
                        } else {
                            System.out.println("消息唯一标识:" + deliveryTag + " 消息 NACK");
                            confirmSet.remove(deliveryTag);
                        }

                        if (confirmSet.contains(deliveryTag)) {
                            // 从数据库捞记录重发消息
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });

            // 4. 发送消息
            for (int i = 0; i < size; i++) {
                long deliveryTag = channel.getNextPublishSeqNo();
                String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
                confirmSet.add(deliveryTag);
                Thread.sleep(1000); // 延迟, 保证 multiple 为 false
            }

            latch.await();
            Assertions.assertTrue(confirmSet.isEmpty());
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
