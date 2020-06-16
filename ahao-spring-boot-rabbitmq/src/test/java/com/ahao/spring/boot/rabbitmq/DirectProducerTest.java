package com.ahao.spring.boot.rabbitmq;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.domain.entity.BaseDO;
import com.ahao.util.spring.SpringContextHolder;
import com.ahao.util.spring.mq.RabbitMQHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitConfig.class, RabbitAutoConfiguration.class, SpringContextHolder.class, DirectConsumer.class})
public class DirectProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectConsumer consumer;

    @BeforeEach
    public void beforeEach() {
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
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendInteger() throws Exception {
        Integer msg = 123;

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        BaseDO actual = (BaseDO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
    }

    @Test
    public void sendGenericObject() throws Exception {
        AjaxDTO msg = AjaxDTO.get(1, "测试", 123);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        AjaxDTO actual = (AjaxDTO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getResult(), actual.getResult());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertEquals(msg.getObj(), actual.getObj());
    }

    @Test
    public void sendDelayString() throws Exception {
        String msg = "sendDelay()";

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelayInteger() throws Exception {
        Integer msg = 123;

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelayObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        BaseDO actual = (BaseDO) DirectConsumer.value;
    }

    @Test
    public void sendDelayGenericObject() throws Exception {
        AjaxDTO msg = AjaxDTO.get(1, "测试", 123);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.sendDelay(DirectConsumer.QUEUE_NAME, msg, 5000);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        AjaxDTO actual = (AjaxDTO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getResult(), actual.getResult());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertEquals(msg.getObj(), actual.getObj());
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
        AjaxDTO msg = AjaxDTO.get(1, "测试", 123L);

        DirectConsumer.latch = new CountDownLatch(1);
        RabbitMQHelper.send(DirectConsumer.QUEUE_NAME, msg);
        DirectConsumer.latch.await(10, TimeUnit.SECONDS);

        AjaxDTO actual = (AjaxDTO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getResult(), actual.getResult());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        // =========================== Long 转为了 Integer ==============================
        Assertions.assertNotEquals(msg.getObj(), actual.getObj());
        // =========================== Long 转为了 Integer ==============================
    }
}
