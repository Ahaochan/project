package moe.ahao.spring.boot.rabbitmq;

import moe.ahao.domain.entity.BaseDO;
import moe.ahao.domain.entity.Result;
import moe.ahao.util.spring.SpringContextHolder;
import moe.ahao.util.spring.mq.RabbitMQHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitConfig.class, RabbitAutoConfiguration.class, SpringContextHolder.class, DirectConsumer.class})
public class DirectProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectConsumer consumer;

    @BeforeEach
    public void beforeEach(@Value("${spring.rabbitmq.host}") String host) {
        // TODO 内嵌RabbitMQ，https://github.com/AlejandroRivera/embedded-rabbitmq
        Assumptions.assumeTrue(StringUtils.isNotBlank(host));
        Assertions.assertNotNull(rabbitTemplate);
        Assertions.assertNotNull(consumer);

        String rabbitHost = SpringContextHolder.getValue("spring.rabbitmq.host");
        Assumptions.assumeTrue(StringUtils.isNotBlank(rabbitHost), "需要配置实际的 rabbitmq 地址");
        DirectConsumer.value = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息发送时间:" + sdf.format(new Date()));
    }

    @Test
    public void sendString() throws Exception {
        String msg = "sendString()";

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
        Assertions.assertTrue(success);
    }

    @Test
    public void sendInteger() throws Exception {
        Integer msg = 123;

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
        Assertions.assertTrue(success);
    }

    @Test
    public void sendObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        BaseDO actual = (BaseDO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(success);
    }

    @Test
    public void sendGenericObject() throws Exception {
        Result<Integer> msg = Result.get(1, "测试", 123);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Result<Integer> actual = (Result<Integer>) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getCode(), actual.getCode());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertEquals(msg.getObj(), actual.getObj());
        Assertions.assertTrue(success);
    }

    @Test
    public void sendDelayString() throws Exception {
        String msg = "sendDelay()";

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
        Assertions.assertTrue(success);
    }

    @Test
    public void sendDelayInteger() throws Exception {
        Integer msg = 123;

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
        Assertions.assertTrue(success);
    }

    @Test
    public void sendDelayObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        BaseDO actual = (BaseDO) DirectConsumer.value;
        Assertions.assertTrue(success);
    }

    @Test
    public void sendDelayGenericObject() throws Exception {
        Result<Integer> msg = Result.get(1, "测试", 123);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Result<Integer> actual = (Result<Integer>) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getCode(), actual.getCode());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertEquals(msg.getObj(), actual.getObj());
        Assertions.assertTrue(success);
    }

    @Test
    public void sendDelayError() throws Exception {
        String msg = "sendDelayError()";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 0xffffffffL + 1);
        });
    }

    @Test
    public void sendGenericError() throws Exception {
        Result<Long> msg = Result.get(1, "测试", 123L);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        boolean success = DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Result<Long> actual = (Result<Long>) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getCode(), actual.getCode());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertTrue(success);
        // =========================== Long 转为了 Integer ==============================
        Assertions.assertNotEquals(msg.getObj(), actual.getObj());
        // =========================== Long 转为了 Integer ==============================
    }
}
