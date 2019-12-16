package moe.ahao.spring.boot.log.mq;

import moe.ahao.spring.boot.log.Constants;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

/**
 * AMQP 发送者的消息拦截器
 * 将 MDC 填充到消息头, 需要配合 {@link MDCReceivePostProcessor} 做解码
 * <p>
 * 使用方法 {@link org.springframework.amqp.rabbit.core.RabbitTemplate#addBeforePublishPostProcessors(MessagePostProcessor...)}
 */
public class MDCPublishPostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        // 1. 获取 MDC
        Map<String, String> mdc = MDC.getCopyOfContextMap();
        if (MapUtils.isEmpty(mdc)) {
            return message;
        }

        // 2. 传入消息头
        MessageProperties properties = message.getMessageProperties();
        mdc.forEach((key, value) -> properties.setHeader(Constants.PREFIX + key, value));
        return message;
    }
}
