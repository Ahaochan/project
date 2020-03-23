package com.ahao.spring.boot.datasources.repository;


import com.ahao.spring.boot.datasources.config.DataSourceProperties;

import java.util.HashMap;
import java.util.Map;

public class DataSourcePropertiesMemoryImpl implements DataSourcePropertiesRepository {
    @Override
    public Map<String, DataSourceProperties> getDataSourceProperties() {
        Map<String, DataSourceProperties> map = new HashMap<>();

        DataSourceProperties properties1 = new DataSourceProperties();
        properties1.setUrl("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db1.sql';");
        map.put("master_1", properties1);

        DataSourceProperties properties2 = new DataSourceProperties();
        properties2.setUrl("jdbc:h2:mem:db2;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db2.sql';");
        map.put("slave_1", properties2);
        DataSourceProperties properties3 = new DataSourceProperties();
        properties3.setUrl("jdbc:h2:mem:db3;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db3.sql';");
        map.put("slave_2", properties3);
        return map;
    }
}
