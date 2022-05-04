package moe.ahao.spring.boot.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitConfig.class, RabbitAutoConfiguration.class})

@EnableRabbit
public class SpringTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void beforeEach(@Value("${spring.rabbitmq.host}") String host) {
        // TODO 内嵌RabbitMQ，https://github.com/AlejandroRivera/embedded-rabbitmq
        Assumptions.assumeTrue(StringUtils.isNotBlank(host), "需要配置实际的 rabbitmq 地址");
    }

    @Test
    public void confirm() throws Exception {
        String msg = "sendString()";

        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("接收: " + correlationData + ", ack:[" + ack + "], cause:[" + cause + "]");
            latch.countDown();
        });
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                // 不能在这里设置 ConfirmCallback
                CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                System.out.println("发送:" + msg + ", correlationId: " + correlationId);
                rabbitTemplate.convertAndSend(DirectConsumer.QUEUE_NAME, (Object) msg, correlationId);
            }).start();
        }

        boolean success = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(success);
    }

    @Test
    public void _return() throws Exception {
        String msg = "sendString()";

        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("接收: " + message + ", replyCode:[" + replyCode + "], replyText:[" + replyText + "], exchange:[" + exchange + "], routingKey:[" + routingKey + "]");
            latch.countDown();
        });
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                // 不能在这里设置 ReturnCallback
                CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                System.out.println("发送:" + msg + ", correlationId: " + correlationId);
                rabbitTemplate.convertAndSend("UNKNOWN", (Object) msg, correlationId);
            }).start();
        }

        boolean success = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(success);
    }
}
