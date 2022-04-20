package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullStatus;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class RocketMQNativeTest {
    public static final String NS = "192.168.19.128:9876";
    public static final String TOPIC = "AhaoTopic1";
    public static final String PRODUCT_GROUP = "ahao-product-group";

    @Test
    void simpleProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "simpleProducer", ("simpleProducer" + i).getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(msg);
            System.out.println("第" + i + "条消息发送结果:" + sendResult);
        }
        producer.shutdown();
    }

    @Test
    void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 10;
        final CountDownLatch2 latch2 = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            Message msg = new Message(TOPIC, "asyncProducer", ("asyncProducer" + i).getBytes(StandardCharsets.UTF_8));
            // SendCallback 接收异步返回结果的回调, 发送顺序不固定
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    latch2.countDown();
                    System.out.println("第" + index + "条消息发送结果:" + sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    latch2.countDown();
                    System.out.println("第" + index + "条消息发送异常:" + e.toString());
                    e.printStackTrace();
                }
            });
        }
        latch2.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }

    @Test
    void onewayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "onewayProducer", ("onewayProducer" + i).getBytes(StandardCharsets.UTF_8));
            producer.sendOneway(msg);
        }
        producer.shutdown();
    }

    @Test
    void orderlyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();

        for (int i = 0; i < 10; i++) {
            int value = 400 + i;
            Message msg = new Message(TOPIC, "orderlyProducer", ("orderlyProducer" + value).getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> messageQueues, Message msg, Object arg) {
                    Integer value = (Integer) arg;
                    long index = value % messageQueues.size();
                    return messageQueues.get((int) index);
                }
            }, value);
            System.out.println("第" + i + "条消息发送结果:" + sendResult);
        }
        producer.shutdown();
    }

    @Test
    void delayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "delayProducer", ("delayProducer" + i).getBytes(StandardCharsets.UTF_8));
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            // org/apache/rocketmq/store/config/MessageStoreConfig.java
            String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
            msg.setDelayTimeLevel(1);
            SendResult sendResult = producer.send(msg);
            System.out.println("第" + i + "条消息发送结果:" + sendResult);
        }
        producer.shutdown();
    }

    @Test
    void batchProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.start();

        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "batchProducer", ("batchProducer" + i).getBytes(StandardCharsets.UTF_8));
            messageList.add(msg);
        }
        SendResult sendResult = producer.send(messageList);
        System.out.println("批量消息发送结果:" + sendResult);
        producer.shutdown();
    }

    @Test
    void transactionProducer() throws Exception {
        Map<String, Boolean> flagMap = new HashMap<>();

        TransactionMQProducer producer = new TransactionMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(NS);
        producer.setExecutorService(Executors.newFixedThreadPool(10));
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                flagMap.put(body, true);
                System.out.println("本地事务执行成功:" + msg.toString());
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                Boolean result = flagMap.get(body);
                System.out.println("检查本地事务:" + result + ": " + msg);
                return Boolean.TRUE.equals(result) ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "transactionProducer", ("transactionProducer" + i).getBytes(StandardCharsets.UTF_8));
            // 事务消息不支持延时消息和批量消息
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            System.out.println("第" + i + "条消息发送结果:" + sendResult);
        }

        Thread.sleep(10000);
        producer.shutdown();
    }

    @Test
    void pullConsumer() throws Exception {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("ahao-consumer-group");
        consumer.setNamesrvAddr(NS);
        consumer.start();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(TOPIC);
        for (MessageQueue messageQueue : messageQueues) {
            PullResult pullResult = consumer.pullBlockIfNotFound(messageQueue, null, 0, Integer.MAX_VALUE);
            System.out.println("本次" + messageQueue + "拉取消息状态:" + pullResult.getPullStatus());
            if (Objects.equals(PullStatus.FOUND, pullResult.getPullStatus())) {
                List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                for (MessageExt m : messageExtList) {
                    System.out.println(new String(m.getBody()));
                }
            }
        }
    }

    @Test
    public void pushConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ahao-consumer-group");
        consumer.setNamesrvAddr(NS);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe(TOPIC, "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
                for (MessageExt m : messageExtList) {
                    System.out.println(Thread.currentThread().getName() + ": " + new String(m.getBody(), StandardCharsets.UTF_8));
                }
                System.out.println("");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        Thread.sleep(10000);
    }

    @Test
    public void orderlyConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ahao-consumer-group");
        consumer.setNamesrvAddr(NS);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe(TOPIC, "*");

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
                for (MessageExt m : messageExtList) {
                    System.out.println(Thread.currentThread().getName() + ": " + m.getQueueId() + ": " + new String(m.getBody(), StandardCharsets.UTF_8));
                }
                System.out.println("");
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();

        Thread.sleep(10000);
    }
}
