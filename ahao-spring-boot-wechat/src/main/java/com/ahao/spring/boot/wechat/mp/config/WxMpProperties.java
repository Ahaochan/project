package com.ahao.spring.boot.wechat.mp.config;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("wechat.mp")
public class WxMpProperties {

    private List<WxMpInMemoryConfigStorage> properties;

    public List<WxMpInMemoryConfigStorage> getProperties() {
        return properties;
    }

    public void setProperties(List<WxMpInMemoryConfigStorage> properties) {
        this.properties = properties;
    }
}
