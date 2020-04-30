package com.ahao.spring.boot.datasources.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * COPY FROM {@link org.springframework.boot.autoconfigure.jdbc.DataSourceProperties}
 */
public class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {
    private Map<String, Object> externalProperties = new HashMap<>(16);

    public Map<String, Object> getExternalProperties() {
        return externalProperties;
    }

    public void setExternalProperties(Map<String, Object> externalProperties) {
        this.externalProperties = externalProperties;
    }
}
