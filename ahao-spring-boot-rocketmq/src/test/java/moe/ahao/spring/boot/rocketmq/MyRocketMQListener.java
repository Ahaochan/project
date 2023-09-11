package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

import java.util.ArrayList;
import java.util.List;


@RocketMQMessageListener(consumerGroup = "MyRocketMQListenerConsumerGroup", topic = RocketMQTemplateTest.TOPIC)
public class MyRocketMQListener implements RocketMQListener<String> {
    public List<String> receiveList = new ArrayList<>();

    @Override
    public void onMessage(String message) {
        receiveList.add(message);
        System.out.println("接收到:" + message);
    }
}
