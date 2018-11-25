package com.ahao.web.module.upload.config;

import com.ahao.web.module.upload.config.properties.UploadProperties;
import com.ahao.web.module.upload.service.UploadService;
import com.ahao.web.module.upload.service.impl.UploadServiceLocalImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {

    @Bean
    @ConditionalOnProperty(name = "ahao.web.upload.profile", havingValue = "local", matchIfMissing = true)
    public UploadService uploadServiceLocalImpl(UploadProperties uploadProperties) {
        return new UploadServiceLocalImpl(uploadProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "ahao.web.upload.profile", havingValue = "remote")
    public UploadService uploadServiceRemoteImpl() {
        return null;
    }
}
