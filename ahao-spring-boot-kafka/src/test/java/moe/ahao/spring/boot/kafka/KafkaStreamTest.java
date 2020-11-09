package moe.ahao.spring.boot.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaStreamTest {
    public static final String HOST = "127.0.0.1:9092";
    public static final boolean AUTO_COMMIT = false;
    public static final String TOPIC_IN = "ahao-in-topic";
    public static final String TOPIC_OUT = "ahao-out-topic";

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

        NewTopic inTopic = new NewTopic(TOPIC_IN, 1, (short) 1);
        NewTopic outTopic = new NewTopic(TOPIC_OUT, 1, (short) 1);
        CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(inTopic, outTopic));
        createTopicsResult.all().get();
        System.out.println("创建Topics:" + createTopicsResult);
    }

    @AfterAll
    public static void afterAll() throws Exception {
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TOPIC_IN, TOPIC_OUT));
        deleteTopicsResult.all().get();
        System.out.println("删除Topics:" + deleteTopicsResult);

        if (adminClient != null) adminClient.close();
        if (broker != null) broker.destroy();
    }

    @Test
    public void stream() throws Exception {
        // 1. 部署 steam 节点
        Properties streamProperties = initStreamProperties();
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(TOPIC_IN);
        KTable<String, Long> count = source
            .mapValues(v -> v.toLowerCase())
            .mapValues(v -> StringUtils.split(v, ' '))
            .flatMapValues(v -> Arrays.asList(v))
            .peek((k, v) -> System.out.printf("peek: %s:%s%n", k, v))
            .groupBy((k, v) -> v)
            .count();
        count.toStream().to(TOPIC_OUT, Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), streamProperties);
        streams.start();

        // 2. 生产消息
        // 3. 消费消息
    }

    private static Properties initStreamProperties() {
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "ahao-app");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return prop;
    }
}
