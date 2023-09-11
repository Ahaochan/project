package moe.ahao.spring.boot.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;

import java.nio.charset.StandardCharsets;

/**
 * 抽象的消费者MessageListener组件
 * 实现RocktMQ原生的RocketMQListener
 */
@Slf4j
public abstract class AbstractRocketMQListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        try {
            log.info("接收到MQ消息开始, message:{}", message);

            this.onMessage(new String(message.getBody(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("接收到MQ消息, 消费MQ消息异常, message:{}", message, e);
        }
    }

    public abstract void onMessage(String message);
}
