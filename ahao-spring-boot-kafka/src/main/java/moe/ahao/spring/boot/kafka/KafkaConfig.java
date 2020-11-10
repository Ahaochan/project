package moe.ahao.spring.boot.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

@Configuration
@EnableKafka
public class KafkaConfig {
    public static final String TOPIC_NAME = "ahao-topic";
    public static final String GROUP_NAME = "ahao-group";
    @Bean
    public NewTopic ahaoTopic() {
        return new NewTopic(TOPIC_NAME,1, (short) 1);
    }

    @Bean
    @ConditionalOnMissingBean(ProducerListener.class)
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }
}
