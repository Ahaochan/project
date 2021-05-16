package moe.ahao.spring.boot.datasources.strategy;

import moe.ahao.util.commons.lang.RandomHelper;

import javax.sql.DataSource;
import java.util.List;

/**
 * 负载均衡算法, 用随机数实现
 */
public class RandomStrategy implements LoadBalanceStrategy {

    @Override
    public DataSource determineDataSource(String groupName, List<DataSource> dataSources) {
        int i = RandomHelper.getInt(dataSources.size());
        return dataSources.get(i);
    }
}
