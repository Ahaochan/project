package moe.ahao.spring.boot.rocketmq;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.exception.BizException;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
    namespace = "${rocketmq.consumer.namespace}",
    topic = Constant.DEFAULT_TOPIC,
    consumerGroup = Constant.CONSUMER_GROUP_TAG2,
    selectorExpression = Constant.TAG2,
    consumeMode = ConsumeMode.CONCURRENTLY,
    messageModel = MessageModel.CLUSTERING,
    consumeThreadNumber = 1
)
public class Event2MQListener extends AbstractRocketMQListener {
    @Override
    public void onMessage(String message) {
        log.info("事件监听器Event2MQListener, 接收到message:{}", message);
        try {

        } catch (BizException e) {
            log.error("业务失败", e);
            // throw e;
        } catch (Exception e) {
            log.error("异常失败", e);
            throw e; // nack
        }
    }
}
