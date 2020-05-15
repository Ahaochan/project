package com.ahao.spring.boot.rabbitmq.processor;

import org.springframework.amqp.core.MessagePostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * MessagePostProcessor 收集器, 在 {@link RabbitBeanPostProcessor} 中统一设置
 */
public class MessageProcessorCollector {
    private List<MessagePostProcessor> templateBeforeMessagePostProcessorList;
    private List<MessagePostProcessor> templateAfterMessagePostProcessorList;
    private List<MessagePostProcessor> factoryBeforeMessagePostProcessorList;
    private List<MessagePostProcessor> factoryAfterMessagePostProcessorList;

    public MessageProcessorCollector() {
        this.templateBeforeMessagePostProcessorList = new ArrayList<>(16);
        this.templateAfterMessagePostProcessorList = new ArrayList<>(16);
        this.factoryBeforeMessagePostProcessorList = new ArrayList<>(16);
        this.factoryAfterMessagePostProcessorList = new ArrayList<>(16);
    }

    public void setTemplateBeforeMessagePostProcessorList(List<MessagePostProcessor> templateBeforeMessagePostProcessorList) {
        this.templateBeforeMessagePostProcessorList = templateBeforeMessagePostProcessorList;
    }

    public void setTemplateAfterMessagePostProcessorList(List<MessagePostProcessor> templateAfterMessagePostProcessorList) {
        this.templateAfterMessagePostProcessorList = templateAfterMessagePostProcessorList;
    }

    public void setFactoryBeforeMessagePostProcessorList(List<MessagePostProcessor> factoryBeforeMessagePostProcessorList) {
        this.factoryBeforeMessagePostProcessorList = factoryBeforeMessagePostProcessorList;
    }

    public void setFactoryAfterMessagePostProcessorList(List<MessagePostProcessor> factoryAfterMessagePostProcessorList) {
        this.factoryAfterMessagePostProcessorList = factoryAfterMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getTemplateBeforeMessagePostProcessorList() {
        return templateBeforeMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getTemplateAfterMessagePostProcessorList() {
        return templateAfterMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryBeforeMessagePostProcessorList() {
        return factoryBeforeMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryAfterMessagePostProcessorList() {
        return factoryAfterMessagePostProcessorList;
    }

    public MessagePostProcessor[] getTemplateBeforeMessagePostProcessorArray() {
        return this.getTemplateBeforeMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getTemplateAfterMessagePostProcessorArray() {
        return this.getTemplateAfterMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getFactoryBeforeMessagePostProcessorArray() {
        return this.getFactoryBeforeMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getFactoryAfterMessagePostProcessorArray() {
        return this.getFactoryAfterMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }


}
