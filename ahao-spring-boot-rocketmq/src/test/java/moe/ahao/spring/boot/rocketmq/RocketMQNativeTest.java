package moe.ahao.spring.boot.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullStatus;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RocketMQNativeTest {
    public static final String NS = "192.168.153.134:19876";
    public static final String TOPIC = "AhaoTopic";

    @Test
    public void product() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ahao-group");
        producer.setNamesrvAddr(NS);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, "TagA", ("hello" + i).getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }
    }

    @Test
    public void pushConsumer() throws Exception {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("ahao-group");
        consumer.setNamesrvAddr(NS);
        consumer.start();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(TOPIC);
        for (MessageQueue messageQueue : messageQueues) {
            PullResult pullResult = consumer.pullBlockIfNotFound(messageQueue, null, 0, Integer.MAX_VALUE);
            System.out.println("本次" + messageQueue + "拉取消息状态:" + pullResult.getPullStatus());
            if (Objects.equals(PullStatus.FOUND, pullResult.getPullStatus())) {
                List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                for (MessageExt m : messageExtList) {
                    System.out.println(new String(m.getBody()));
                }
            }
        }
    }
}
