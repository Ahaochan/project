package com.ahao.spring.boot.rabbitmq;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RabbitBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof RabbitTemplate) {
            warpRabbitTemplate((RabbitTemplate) bean, beanName);
        }
        if (bean instanceof AbstractRabbitListenerContainerFactory) {
            warpContainerFactory((AbstractRabbitListenerContainerFactory) bean, beanName);
        }
        return bean;
    }

    private void warpContainerFactory(AbstractRabbitListenerContainerFactory bean, String beanName) {
        // bean.setAfterReceivePostProcessors();
    }

    private void warpRabbitTemplate(RabbitTemplate bean, String beanName) {
        // bean.setAfterReceivePostProcessors();
        // bean.setBeforePublishPostProcessors();

        // bean.setConfirmCallback();
        // bean.setReturnCallback();
        // bean.setRecoveryCallback();
    }
}
