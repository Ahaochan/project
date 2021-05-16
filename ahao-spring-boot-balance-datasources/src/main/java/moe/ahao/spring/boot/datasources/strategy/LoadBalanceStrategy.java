package moe.ahao.spring.boot.datasources.strategy;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源切换规则
 */
public interface LoadBalanceStrategy {

    /**
     * 根据算法选出一个数据源
     * @param groupName   分组名
     * @param dataSources 数据源选择库
     * @return dataSource 所选择的数据源
     */
    DataSource determineDataSource(String groupName, List<DataSource> dataSources);
}
