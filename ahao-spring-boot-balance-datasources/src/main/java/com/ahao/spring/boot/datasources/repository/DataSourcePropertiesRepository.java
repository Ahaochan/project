package com.ahao.spring.boot.datasources.repository;


import com.ahao.spring.boot.datasources.properties.ExDataSourceProperties;

import java.util.Map;

public interface DataSourcePropertiesRepository {
    Map<String, ExDataSourceProperties> getDataSourceProperties();
}
