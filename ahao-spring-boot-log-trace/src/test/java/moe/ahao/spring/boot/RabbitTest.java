package moe.ahao.spring.boot;

import moe.ahao.spring.boot.dependency.TestConfig;
import moe.ahao.spring.boot.dependency.TestRabbitConsumer;
import moe.ahao.spring.boot.log.Constants;
import moe.ahao.spring.boot.util.IDGenerator;
import moe.ahao.util.spring.SpringContextHolder;
import moe.ahao.util.spring.mq.RabbitMQHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitAutoConfiguration.class, SpringContextHolder.class, TestRabbitConsumer.class, TestConfig.class})
@ActiveProfiles("rabbit")
@EnableRabbit
public class RabbitTest {
    private static final Logger logger = LoggerFactory.getLogger(RabbitTest.class);

    @Autowired
    private AbstractRabbitListenerContainerFactory factory;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TestRabbitConsumer consumer;

    @BeforeEach
    public void before() {
        Assertions.assertNotNull(rabbitTemplate);
        Assertions.assertNotNull(consumer);

        String rabbitHost = SpringContextHolder.getValue("spring.rabbitmq.host");
        // TODO 内嵌RabbitMQ，https://github.com/AlejandroRivera/embedded-rabbitmq
        Assumptions.assumeTrue(StringUtils.isNotBlank(rabbitHost), "需要配置实际的 rabbitmq 地址");
        TestRabbitConsumer.value = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息发送时间:" + sdf.format(new Date()));
    }

    @Test
    public void test() throws InterruptedException {
        MDC.put(Constants.TRACE_ID, IDGenerator.generateID(Constants.TRACE_ID));
        String value = "hello";
        logger.debug("发送消息{}到{}", value, TestRabbitConsumer.QUEUE_NAME);
        RabbitMQHelper.send(TestRabbitConsumer.QUEUE_NAME, value);

        boolean success = TestRabbitConsumer.latch.await(10, TimeUnit.SECONDS);
        Assertions.assertEquals(value, TestRabbitConsumer.value);
        Assertions.assertTrue(success);
    }
}
