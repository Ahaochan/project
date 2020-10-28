package moe.ahao.spring.boot.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaNativeTest {
    public static final String HOST = "127.0.0.1:9092";
    public static final boolean AUTO_COMMIT = false;

    private static EmbeddedKafkaBroker broker;
    private static AdminClient adminClient;
    public static final int SIZE = 100;
    public static final int PARTITION = 2;

    @BeforeAll
    public static void beforeAll() throws Exception {
        broker = new EmbeddedKafkaBroker(1).kafkaPorts(9092);
        broker.afterPropertiesSet();

        Map<String, Object> adminProperties = new HashMap<>();
        adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        adminClient = AdminClient.create(adminProperties);
    }

    @AfterAll
    public static void afterAll() throws Exception {
        if (adminClient != null) adminClient.close();
        if (broker != null) broker.destroy();
    }

    @BeforeEach
    public void beforeEach() {
        // 1. 生产消息
        Map<String, Object> producerProperties = KafkaNativeTest.initProducerProperties();
        // 2. 指定分区器
        producerProperties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SimplePartitioner.class.getName());
        try (Producer<String, String> producer = new KafkaProducer<>(producerProperties);) {
            for (int i = 0; i < SIZE; i++) {
                String partitionKey = "key" + i;
                String value = "value" + i;

                ProducerRecord<String, String> record = new ProducerRecord<>(KafkaConfig.TOPIC_NAME, partitionKey, value);
                producer.send(record, (recordMetadata, e) -> {
                    if (e != null) {
                        System.out.println("==================>" + "消息" + value + "发送失败");
                        e.printStackTrace();
                    } else {
                        System.out.println("==================>" + "消息" + value + "发送成功, offset:" + recordMetadata.offset() + ", partition:" + recordMetadata.partition());
                    }
                });
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "my-topic")
    @Disabled
    public void topic(String topicName) throws Exception {
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
    public void consumerSync() throws Exception {
        // 1. 初始化消费者
        Map<String, Object> prop = KafkaNativeTest.initConsumerProperties();
        Consumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(KafkaConfig.TOPIC_NAME)); // 订阅几个 topic
        // consumer.assign(Arrays.asList(new TopicPartition(KafkaConfig.TOPIC_NAME, 0))); // 订阅 topic 的某个 partition
        boolean autoCommit = Boolean.getBoolean(prop.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG).toString());

        // 2. 拉取所有消息
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10)); // 拉取 10s 的消息
        System.out.println("拉取消息总数:" + records.count());
        Assertions.assertEquals(SIZE, records.count());

        // 3. 根据 partition 分区
        CountDownLatch latch = new CountDownLatch(PARTITION);

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
    public void consumerThreadPool() throws Exception {
        // 1. 初始化消费者
        Map<String, Object> prop = KafkaNativeTest.initConsumerProperties();
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(true));
        Consumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(KafkaConfig.TOPIC_NAME)); // 订阅几个 topic
        // consumer.assign(Arrays.asList(new TopicPartition(KafkaConfig.TOPIC_NAME, 0))); // 订阅 topic 的某个 partition
        boolean autoCommit = Boolean.parseBoolean(prop.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG).toString());

        // 2. 拉取所有消息
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10)); // 拉取 10s 的消息
        System.out.println("拉取消息总数:" + records.count());
        Assertions.assertEquals(SIZE, records.count());

        // 3. 根据 partition 分区
        CountDownLatch latch = new CountDownLatch(PARTITION);
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

    private static Map<String, Object> initProducerProperties() {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        prop.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(0));
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16384));
        prop.put(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(1));
        prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(33554432));
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return prop;
    }

    private static Map<String, Object> initConsumerProperties() {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(AUTO_COMMIT));
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(100));
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(15000));
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return prop;
    }

    public static class SimplePartitioner implements Partitioner {

        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            String keyStr = String.valueOf(key);
            int index = Integer.parseInt(keyStr.substring(3)) % PARTITION;
            System.out.println("==================>" + "[" + value + "]消息投递到第[" + index + "]个partition");
            return index;
        }

        @Override
        public void close() {

        }

        @Override
        public void configure(Map<String, ?> configs) {

        }
    }
}
