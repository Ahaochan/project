package com.ahao.spring.quartz.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.quartz")
public class QuartzProperties {

    private String invokeMethod;
    private Map<String, String> cronTask;

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public Map<String, String> getCronTask() {
        return cronTask;
    }

    public void setCronTask(Map<String, String> cronTask) {
        this.cronTask = cronTask;
    }
}

