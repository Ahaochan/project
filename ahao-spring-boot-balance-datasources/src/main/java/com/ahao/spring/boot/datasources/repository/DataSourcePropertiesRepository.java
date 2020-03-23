package com.ahao.spring.boot.datasources.repository;


import com.ahao.spring.boot.datasources.config.DataSourceProperties;

import java.util.Map;

public interface DataSourcePropertiesRepository {
    Map<String, DataSourceProperties> getDataSourceProperties();
}
