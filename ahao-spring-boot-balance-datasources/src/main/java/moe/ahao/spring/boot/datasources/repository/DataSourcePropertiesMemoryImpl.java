package moe.ahao.spring.boot.datasources.repository;


import moe.ahao.spring.boot.datasources.properties.ExDataSourceProperties;

import java.util.HashMap;
import java.util.Map;

public class DataSourcePropertiesMemoryImpl implements DataSourcePropertiesRepository {
    @Override
    public Map<String, ExDataSourceProperties> getDataSourceProperties() {
        Map<String, ExDataSourceProperties> map = new HashMap<>();

        ExDataSourceProperties properties1 = new ExDataSourceProperties();
        properties1.setUrl("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db1.sql';");
        map.put("master_1", properties1);

        ExDataSourceProperties properties2 = new ExDataSourceProperties();
        properties2.setUrl("jdbc:h2:mem:db2;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db2.sql';");
        map.put("slave_1", properties2);
        ExDataSourceProperties properties3 = new ExDataSourceProperties();
        properties3.setUrl("jdbc:h2:mem:db3;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db3.sql';");
        map.put("slave_2", properties3);
        return map;
    }
}
