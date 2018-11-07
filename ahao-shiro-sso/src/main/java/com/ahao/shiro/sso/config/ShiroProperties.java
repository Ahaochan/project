package com.ahao.shiro.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = ShiroProperties.PREFIX)
public class ShiroProperties {
    public static final String PREFIX = "ahao.sso";

    private boolean newSessionId;



    public boolean isNewSessionId() {
        return newSessionId;
    }

    public void setNewSessionId(boolean newSessionId) {
        this.newSessionId = newSessionId;
    }
}
