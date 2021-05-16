package moe.ahao.spring.boot.datasources.repository;


import moe.ahao.spring.boot.datasources.properties.ExDataSourceProperties;

import java.util.Map;

public interface DataSourcePropertiesRepository {
    Map<String, ExDataSourceProperties> getDataSourceProperties();
}
