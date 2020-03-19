package com.ahao.spring.boot.datasources.repository;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.Map;

public interface DataSourcePropertiesRepository {
    Map<String, DataSourceProperties> getDataSourceProperties();
}
