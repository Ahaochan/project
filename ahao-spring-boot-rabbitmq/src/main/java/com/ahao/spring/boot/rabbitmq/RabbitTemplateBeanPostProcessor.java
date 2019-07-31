package com.ahao.spring.boot.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RabbitTemplateBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RabbitTemplate) {
            RabbitTemplate rabbitTemplate = (RabbitTemplate) bean;
//            rabbitTemplate.setConfirmCallback();
//            rabbitTemplate.setReturnCallback();
//            rabbitTemplate.setRecoveryCallback();
        }
        return bean;
    }
}
