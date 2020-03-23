package com.ahao.spring.boot.datasources.config;

import java.util.Collections;
import java.util.Map;

/**
 * COPY FROM {@link org.springframework.boot.autoconfigure.jdbc.DataSourceProperties}
 */
public class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {
    private Map<String, Object> externalProperties = Collections.emptyMap();

    public Map<String, Object> getExternalProperties() {
        return externalProperties;
    }

    public void setExternalProperties(Map<String, Object> externalProperties) {
        this.externalProperties = externalProperties;
    }
}
