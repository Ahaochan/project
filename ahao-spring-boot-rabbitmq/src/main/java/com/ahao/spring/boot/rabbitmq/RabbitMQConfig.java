package com.ahao.spring.boot.rabbitmq;

import com.ahao.util.spring.mq.RabbitMQHelper;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        FastJsonMessageConverter converter = new FastJsonMessageConverter();
        converter.setCharset(StandardCharsets.UTF_8);
        converter.setUseRawJson(false);
        return converter;
    }

    @Bean(RabbitMQHelper.DELAY_EXCHANGE_NAME)
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQHelper.DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }
}
