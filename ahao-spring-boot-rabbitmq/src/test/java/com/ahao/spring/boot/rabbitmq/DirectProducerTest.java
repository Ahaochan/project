package com.ahao.spring.boot.rabbitmq;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.domain.entity.BaseDO;
import com.ahao.util.spring.SpringContextHolder;
import com.ahao.util.spring.mq.RabbitMQHelper;
import com.rabbitmq.client.*;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RabbitMQConfig.class, RabbitAutoConfiguration.class, SpringContextHolder.class, DirectConsumer.class})
public class DirectProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectConsumer consumer;

    @BeforeEach
    public void before() {
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
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendInteger() throws Exception {
        Integer msg = 123;
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setId(123L);
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        BaseDO actual = (BaseDO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getId(), actual.getId());
        Assertions.assertEquals(msg.getCreateTime(), actual.getCreateTime());
        Assertions.assertEquals(msg.getUpdateTime(), actual.getUpdateTime());
    }

    @Test
    public void sendGenericObject() throws Exception {
        AjaxDTO msg = AjaxDTO.get(1, "测试", 123);
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        AjaxDTO actual = (AjaxDTO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getResult(), actual.getResult());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        Assertions.assertEquals(msg.getObj(), actual.getObj());
    }

    @Test
    public void sendDelayString() throws Exception {
        String msg = "sendDelay()";
        String routingKey = DirectConsumer.QUEUE_NAME;
        RabbitMQHelper.sendDelay(routingKey, msg, 3000);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(10000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelayInteger() throws Exception {
        Integer msg = 123;
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.sendDelay(routingKey, msg, 3000);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(10000);
        Assertions.assertEquals(msg, DirectConsumer.value);
    }

    @Test
    public void sendDelayObject() throws Exception {
        BaseDO msg = new BaseDO();
        msg.setId(123L);
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.sendDelay(routingKey, msg, 3000);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(10000);
        BaseDO actual = (BaseDO) DirectConsumer.value;
        Assertions.assertEquals(msg.getId(), actual.getId());
        Assertions.assertEquals(msg.getCreateTime(), actual.getCreateTime());
        Assertions.assertEquals(msg.getUpdateTime(), actual.getUpdateTime());
    }

    @Test
    public void sendDelayGenericObject() throws Exception {
        AjaxDTO msg = AjaxDTO.get(1, "测试", 123);
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.sendDelay(routingKey, msg, 3000);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(10000);
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
        String routingKey = DirectConsumer.QUEUE_NAME;

        RabbitMQHelper.send(routingKey, msg);

        Assertions.assertNull(DirectConsumer.value);
        Thread.sleep(2000);
        AjaxDTO actual = (AjaxDTO) DirectConsumer.value;
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(msg.getResult(), actual.getResult());
        Assertions.assertEquals(msg.getMsg(), actual.getMsg());
        // =========================== Long 转为了 Integer ==============================
        Assertions.assertNotEquals(msg.getObj(), actual.getObj());
        // =========================== Long 转为了 Integer ==============================
    }
}
