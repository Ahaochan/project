package com.ahao.spring.boot.wechat.mp;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceOkHttpImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxMpConfig {

    @Bean
    @ConfigurationProperties("wechat.mp")
    public WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage() {
        return new WxMpInMemoryConfigStorage();
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage config) {
        WxMpService wxService = new WxMpServiceOkHttpImpl();
        wxService.setWxMpConfigStorage(config);
        return wxService;
    }
}
