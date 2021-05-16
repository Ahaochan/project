package moe.ahao.spring.boot.datasources.properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * COPY FROM {@link org.springframework.boot.autoconfigure.jdbc.DataSourceProperties}
 */
public class ExDataSourceProperties extends DataSourceProperties {
    private Map<String, Object> externalProperties = new HashMap<>(16);

    public Map<String, Object> getExternalProperties() {
        return externalProperties;
    }

    public void setExternalProperties(Map<String, Object> externalProperties) {
        this.externalProperties = externalProperties;
    }
}
