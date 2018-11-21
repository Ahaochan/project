package com.ahao.web.module.upload.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = UploadProperties.PREFIX)
public class UploadProperties {
    public static final String PREFIX = "ahao.web.upload";

    private String fileUploadPath;
    private String filePrefixPath;

    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public String getFilePrefixPath() {
        return filePrefixPath;
    }

    public void setFilePrefixPath(String filePrefixPath) {
        this.filePrefixPath = filePrefixPath;
    }
}
