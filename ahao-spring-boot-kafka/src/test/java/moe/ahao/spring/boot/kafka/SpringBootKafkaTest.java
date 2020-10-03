package moe.ahao.spring.boot.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EmbeddedKafka(ports = 9092)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {KafkaConfig.class, KafkaAutoConfiguration.class, SpringBootKafkaTest.TestConfig.class})

@EnableKafka
public class SpringBootKafkaTest {
    public static class TestConfig {
        private static CountDownLatch latch;
        private static Object value;

        @KafkaListener(topics = KafkaConfig.TOPIC_NAME, id = KafkaConfig.GROUP_NAME)
        public void consumer(ConsumerRecord<String, String> record) throws Exception {
            try {
                String msg = record.value();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("消息接收时间:" + sdf.format(new Date()));
                System.out.println("接收到的消息:" + msg);
                Thread.sleep(1000);
                value = msg;

                // channel.basicAck(tag, false);
            } catch (Exception e) {
                e.printStackTrace();
                // channel.basicNack(tag, false, false);
            } finally {
                latch.countDown();
            }
        }
    }
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaProperties properties;

    @Test
    public void topic() throws Exception {
        Map<String, Object> map = properties.buildAdminProperties();

        try (AdminClient client = AdminClient.create(map)) {
            List<String> topicNameList = Stream.iterate(0, i -> i + 1).limit(10).map(i -> "topic" + i)
                .collect(Collectors.toList());
            List<NewTopic> topicList = Stream.iterate(0, i -> i + 1).limit(10).map(i -> "topic" + i)
                .map(n -> new NewTopic(n, 1, (short) 1)).collect(Collectors.toList());

            CreateTopicsResult createTopicsResult = client.createTopics(topicList);

            ListTopicsResult listTopicsResult = client.listTopics(new ListTopicsOptions());
            listTopicsResult.listings().get().stream().map(TopicListing::name)
                .filter(s -> StringUtils.startsWith(s, "topic"))
                .peek(System.out::println)
                .map(topicNameList::contains)
                .forEach(Assertions::assertTrue);

            // DeleteTopicsResult deleteTopicsResult = client.deleteTopics(topicNameList);
        }
    }

    @Test
    public void sendString() throws Exception {
        String msg = "sendString()";

        TestConfig.latch = new CountDownLatch(1);
        // kafkaTemplate.send(KafkaConfig.TOPIC_NAME, KafkaConfig.GROUP_NAME, msg);
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, msg);
        boolean success = TestConfig.latch.await(10, TimeUnit.SECONDS);


        Assertions.assertEquals(msg, TestConfig.value);
        Assertions.assertTrue(success);
    }
}
