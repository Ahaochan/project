package com.ahao.spring.boot.rabbitmq.processor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class RabbitBeanPostProcessor implements BeanPostProcessor {

    private MessageProcessorCollector messageProcessorCollector;
    public RabbitBeanPostProcessor() {
        this(new MessageProcessorCollector());
    }
    public RabbitBeanPostProcessor(MessageProcessorCollector messageProcessorCollector) {
        this.messageProcessorCollector = messageProcessorCollector;
    }

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
        bean.setBeforeSendReplyPostProcessors(messageProcessorCollector.getFactoryAfterMessagePostProcessorArray());
        bean.setAfterReceivePostProcessors(messageProcessorCollector.getFactoryAfterMessagePostProcessorArray()); // 处理 @RabbitListener
    }

    private void warpRabbitTemplate(RabbitTemplate bean, String beanName) {
        bean.setBeforePublishPostProcessors(messageProcessorCollector.getTemplateBeforeMessagePostProcessorArray());
        bean.setAfterReceivePostProcessors(messageProcessorCollector.getTemplateAfterMessagePostProcessorArray());

        // bean.setConfirmCallback();
        // bean.setReturnCallback();
        // bean.setRecoveryCallback();
    }
}
