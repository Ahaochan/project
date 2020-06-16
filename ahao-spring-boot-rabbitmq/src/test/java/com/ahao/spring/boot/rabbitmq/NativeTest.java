package com.ahao.spring.boot.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NativeTest {
    public static final String EXCHANGE_NAME = "ahao-exchange";
    public static final String ROUTING_KEY = "ahao-routing-key";
    public static final String QUEUE_NAME = "ahao-queue";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    @BeforeEach
    public void beforeEach() throws Exception {
        // 1. 建立连接工厂
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.8.60.56");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 2. 获取连接, 获取 Channel
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        // 3. 创建 Exchange 和 Queue 并绑定
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);
    }

    @AfterEach
    public void afterEach() throws Exception {
        channel.close();
        connection.close();
    }

    @Test
    public void producer() throws Exception {
        // 3. 发送消息
        String msg = "现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void consumer() throws Exception {
        // 1. 消费消息
        CountDownLatch latch = new CountDownLatch(1);
        channel.basicQos(64);
        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接受到:" + new String(body));
                latch.countDown();
            }
        });

        // 2. 等待消息消费后, 再关闭资源
        boolean success = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(success);
    }

    @Test
    public void simpleConfirm() throws Exception {
        // 1. 开启确认模式
        int size = 10;
        channel.confirmSelect();

        // 2. 发送消息
        for (int i = 0; i < size; i++) {
            long deliveryTag = channel.getNextPublishSeqNo();
            String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            if (!channel.waitForConfirms()) {
                System.out.println("消息[" + msg + "]发送失败");
            }
        }
    }

    @Test
    public void batchConfirm() throws Exception {
        // 1. 开启确认模式
        int size = 10;
        channel.confirmSelect();

        // 2. 发送消息
        for (int i = 0; i < size; i++) {
            long deliveryTag = channel.getNextPublishSeqNo();
            String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
        }
        if (!channel.waitForConfirms()) {
            System.out.println("消息发送失败");
        }
    }

    @Test
    public void asyncConfirm() throws Exception {
        // 1. 开启确认模式
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

        // 2. 发送消息
        for (int i = 0; i < size; i++) {
            long deliveryTag = channel.getNextPublishSeqNo();
            String msg = "deliveryTag: " + deliveryTag + ", 现在时间" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            confirmSet.add(deliveryTag);
            Thread.sleep(1000); // 延迟, 保证 multiple 为 false
        }

        boolean success = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(confirmSet.isEmpty());
        Assertions.assertTrue(success);
    }
}
