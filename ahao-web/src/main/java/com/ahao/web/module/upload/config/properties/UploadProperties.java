package com.ahao.web.module.upload.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = UploadProperties.PREFIX)
public class UploadProperties {
    public static final String PREFIX = "ahao.web.upload";

    private String fileSaveUri;
    private String filePrefixUri;

    public String getFileSaveUri() {
        return fileSaveUri;
    }

    public void setFileSaveUri(String fileSaveUri) {
        this.fileSaveUri = fileSaveUri;
    }

    public String getFilePrefixUri() {
        return filePrefixUri;
    }

    public void setFilePrefixUri(String filePrefixUri) {
        this.filePrefixUri = filePrefixUri;
    }
}
