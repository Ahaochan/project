package moe.ahao.spring.boot.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(ports = 9092)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootConfiguration
public class SpringTest {
    public static final String HOST = "127.0.0.1:9092";

    private DefaultKafkaProducerFactory<String, String> producerFactory;
    private KafkaTemplate<String, String> template;

    private KafkaMessageListenerContainer<String, String> container;

    @BeforeEach
    public void beforeEach() throws Exception {
        Map<String, Object> producerProperties = new HashMap<>();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, 0);
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "1");
        producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        producerProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerFactory = new DefaultKafkaProducerFactory<>(producerProperties);
        template = new KafkaTemplate<>(producerFactory);
    }

    @AfterEach
    public void afterEach() throws Exception {
        Optional.ofNullable(producerFactory).ifPresent(DefaultKafkaProducerFactory::destroy);
        Optional.ofNullable(container).ifPresent(AbstractMessageListenerContainer::stop);
    }

    @Test
    public void spring() throws Exception {
        int size = 4;
        CountDownLatch latch = new CountDownLatch(size);

        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        ContainerProperties containerProps = new ContainerProperties(KafkaConfig.TOPIC_NAME);
        containerProps.setMissingTopicsFatal(false);
        containerProps.setMessageListener((MessageListener<Integer, String>) message -> {
            System.out.println("received: " + message);
            latch.countDown();
        });
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.setBeanName("test-bean");
        container.start();

        Thread.sleep(1000); // wait a bit for the container to start
        template.setDefaultTopic(KafkaConfig.TOPIC_NAME);
        for (int i = 0; i < size; i++) {
            template.sendDefault("key" + i, "value" + i);
        }
        template.flush();
        Assertions.assertTrue(latch.await(60, TimeUnit.SECONDS));
    }

    @Test
    public void producerAsync() throws InterruptedException {
        String topic = KafkaConfig.TOPIC_NAME;
        String data = "hello";
        CountDownLatch latch = new CountDownLatch(1);

        ListenableFuture<SendResult<String, String>> future = template.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                latch.countDown();
                ex.printStackTrace();
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                latch.countDown();
                System.out.println("发送成功:" + result.toString());
            }
        });
        template.flush();
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void producerSync() throws Exception {
        String topic = KafkaConfig.TOPIC_NAME;
        String data = "hello";
        CountDownLatch latch = new CountDownLatch(1);

        try {
            SendResult<String, String> result = template.send(topic, data).get(10, TimeUnit.SECONDS);
            System.out.println("发送成功:" + result.toString());
            template.flush();
        } finally {
            latch.countDown();
        }
        Assertions.assertTrue(latch.await(10, TimeUnit.SECONDS));
    }
}
