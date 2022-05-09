package moe.ahao.spring.boot.kafka;

import moe.ahao.embedded.KafkaExtension;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class KafkaNativeTest {
    @RegisterExtension
    static KafkaExtension kafkaExtension = new KafkaExtension();

    @ParameterizedTest
    @ValueSource(strings = "my-topic")
    void topic(String topicName) throws Exception {
        AdminClient adminClient = kafkaExtension.getAdminClient();

        NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
        CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(newTopic));
        createTopicsResult.all().get();
        System.out.println("创建Topics:" + createTopicsResult);

        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        ListTopicsResult listTopicsResult = adminClient.listTopics(listTopicsOptions);
        listTopicsResult.names().get().forEach(s -> System.out.println("查看Topics: " + s));
        listTopicsResult.listings().get().forEach(s -> System.out.println("查看Topics: " + s));
        listTopicsResult.namesToListings().get().forEach((n, r) -> System.out.println("查看Topics: " + "name:" + n + ", topic:" + r));

        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(topicName));
        describeTopicsResult.all().get().forEach((n, r) -> System.out.println("查看Topics配置: " + "name:" + n + ", topic:" + r));

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));
        describeConfigsResult.all().get().forEach((n, c) -> System.out.println("查看Topics配置: " + "name:" + n + ", config:" + c));

        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(topicName));
        deleteTopicsResult.all().get();
        System.out.println("删除Topics:" + deleteTopicsResult);
    }

    /**
     * 每个消费者单独一个线程, 里面 while 循环单独处理消息
     */
    @Test
    void consumerSync() throws Exception {
        // 1. 初始化消费者
        Map<String, Object> prop = kafkaExtension.initConsumerProperties();
        Consumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(KafkaConfig.TOPIC_NAME)); // 订阅几个 topic
        // consumer.assign(Arrays.asList(new TopicPartition(KafkaConfig.TOPIC_NAME, 0))); // 订阅 topic 的某个 partition
        boolean autoCommit = Boolean.getBoolean(prop.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG).toString());

        // 2. 拉取所有消息
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10)); // 拉取 10s 的消息
        System.out.println("拉取消息总数:" + records.count());
        Assertions.assertEquals(KafkaExtension.SIZE, records.count());

        // 3. 根据 partition 分区
        CountDownLatch latch = new CountDownLatch(KafkaExtension.PARTITION);

        List<TopicPartition> topicPartitionList = new ArrayList<>(records.partitions());
        for (TopicPartition topicPartition : topicPartitionList) {
            consumer.seek(topicPartition, 0); // 手动指定 offset 起始位置
            List<ConsumerRecord<String, String>> recordList = records.records(topicPartition);
            int recordsSize = recordList.size();
            System.out.println("拉取消息partition:" + topicPartition.toString() + ", 消息数:" + recordList.size());
            if (recordsSize == 0) {
                continue;
            }

            // 3.1. 处理消息
            for (ConsumerRecord<String, String> record : recordList) {
                System.out.println(Thread.currentThread().getName() + ", 消费消息:" + record.toString());
            }

            // 3.2. 手动提交这个 partition 的 offset
            if (!autoCommit) {
                long newOffset = recordList.get(recordList.size() - 1).offset() + 1;
                Map<TopicPartition, OffsetAndMetadata> offsetMap = Collections.singletonMap(topicPartition, new OffsetAndMetadata(newOffset));
                consumer.commitSync(offsetMap);
                System.out.println("消费消息partition:" + topicPartition.toString() + ", 提交offset:" + newOffset);
            }
            latch.countDown();
        }

        // 3. 等待消息消费后, 再关闭资源
        boolean success = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(success);
    }

    @Test
    void consumerThreadPool() throws Exception {
        // 1. 初始化消费者
        Map<String, Object> prop = kafkaExtension.initConsumerProperties();
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(true));
        Consumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(KafkaConfig.TOPIC_NAME)); // 订阅几个 topic
        // consumer.assign(Arrays.asList(new TopicPartition(KafkaConfig.TOPIC_NAME, 0))); // 订阅 topic 的某个 partition
        boolean autoCommit = Boolean.parseBoolean(prop.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG).toString());

        // 2. 拉取所有消息
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10)); // 拉取 10s 的消息
        System.out.println("拉取消息总数:" + records.count());
        Assertions.assertEquals(KafkaExtension.SIZE, records.count());

        // 3. 根据 partition 分区
        CountDownLatch latch = new CountDownLatch(KafkaExtension.PARTITION);
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        List<TopicPartition> topicPartitionList = new ArrayList<>(records.partitions());
        for (TopicPartition topicPartition : topicPartitionList) {
            consumer.seek(topicPartition, 0); // 手动指定 offset 起始位置
            List<ConsumerRecord<String, String>> recordList = records.records(topicPartition);
            int recordsSize = recordList.size();
            System.out.println("拉取消息partition:" + topicPartition.toString() + ", 消息数:" + recordList.size());
            if (recordsSize == 0) {
                continue;
            }

            // 3.3. 将消息投递到线程池异步处理
            threadPool.submit(() -> {
                try {
                    for (ConsumerRecord<String, String> record : recordList) {
                        System.out.println(Thread.currentThread().getName() + "消费消息:" + record.toString());
                    }
                    // 3.2. TODO 手动提交这个 partition 的 offset
                    if (!autoCommit) {
                        long newOffset = recordList.get(recordList.size() - 1).offset() + 1;
                        Map<TopicPartition, OffsetAndMetadata> offsetMap = Collections.singletonMap(topicPartition, new OffsetAndMetadata(newOffset));
                        consumer.commitSync(offsetMap);
                        System.out.println("消费消息partition:" + topicPartition.toString() + ", 提交offset:" + newOffset);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 3. 等待消息消费后, 再关闭资源
        boolean success = latch.await(10, TimeUnit.MINUTES);
        Assertions.assertTrue(success);
    }
}
