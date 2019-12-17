package moe.ahao.spring.boot.log.mq;

import moe.ahao.spring.boot.log.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

/**
 * AMQP 发送者的消息拦截器
 * 从消息头解析 MDC, 需要配合 {@link RabbitMDCPublishPostProcessor} 做解码
 * <p>
 * 使用方法
 * 1. {@link org.springframework.amqp.rabbit.core.RabbitTemplate#addAfterReceivePostProcessors(MessagePostProcessor...)}
 * 2. {@link org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory#setAfterReceivePostProcessors(MessagePostProcessor...)}
 */
public class RabbitMDCReceivePostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        MessageProperties properties = message.getMessageProperties();
        properties.getHeaders()
            .entrySet()
            .stream()
            .filter(e -> StringUtils.startsWith(e.getKey(), Constants.PREFIX))
            .forEach((e) -> {
                String key = StringUtils.removeStart(e.getKey(), Constants.PREFIX);
                String value = String.valueOf(e.getValue());
                MDC.put(key, value);
            });
        return message;
    }
}
