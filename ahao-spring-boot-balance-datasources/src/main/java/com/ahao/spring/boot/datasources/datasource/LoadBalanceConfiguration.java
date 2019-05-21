package com.ahao.spring.boot.datasources.datasource;

import com.ahao.spring.boot.datasources.DataSourceContextHolder;
import com.ahao.util.commons.lang.RandomHelper;
import com.ahao.util.spring.SpringContextHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@AutoConfigureAfter(value = DataSourceConfiguration.class)
@DependsOn(SpringContextHolder.BEAN_NAME)
public class LoadBalanceConfiguration {
	@Value("${spring.datasource.master-bean-name}")
	private String[] masterNames;

	@Value("${spring.datasource.salve-bean-name}")
	private String[] slaveNames;

	@Bean
	@Primary
	public DataSource dataSource() {
		// 1. 保证 master 数据库不为空
		Validate.noNullElements(masterNames);

		// 2. 负载均衡 路由类
		AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
			@Override
			protected Object determineCurrentLookupKey() {
				boolean isMaster = DataSourceContextHolder.isMaster();
				String key = loadBalance_polling(isMaster);
				logger.info(String.format("当前数据源切换到 %s 数据库: %s", isMaster ? "Master" : "Slave", key));
				return key;
			}
		};

		// 3. 将所有主从数据源加入路由
		Map<Object, Object> targetDataSources = Stream.concat(Arrays.stream(masterNames), Arrays.stream(slaveNames))
				.collect(Collectors.toMap(name  -> name, SpringContextHolder::getBean));
		proxy.setTargetDataSources(targetDataSources);

		// 4. 设置默认库
		Object defaultTargetDataSource = ArrayUtils.getLength(masterNames) > 0 ?
				SpringContextHolder.getBean(masterNames[0]) : null;
		proxy.setDefaultTargetDataSource(defaultTargetDataSource);
		return proxy;
	}

	/**
	 * 负载均衡算法, 用随机数实现
	 * @param isMaster 是否为 master 数据库
	 * @return 加载数据源的 key, 当前使用 BeanName 作为 key
	 */
	public String loadBalance_random(boolean isMaster) {
		String[] names = isMaster ? masterNames : slaveNames;
		int idx = RandomHelper.getInt(names.length);
		return names[idx];
	}

	/**
	 * 负载均衡算法, 轮询实现
	 * @param isMaster 是否为 master 数据库
	 * @return 加载数据源的 key, 当前使用 BeanName 作为 key
	 */
	public String loadBalance_polling(boolean isMaster) {
		String[] names = isMaster ? masterNames : slaveNames;
		AtomicInteger idx = isMaster ? masterIdx : slaveIdx;
		int i = idx.getAndIncrement();
		return names[i % names.length];
	}
	private AtomicInteger masterIdx = new AtomicInteger(0);
	private AtomicInteger slaveIdx = new AtomicInteger(0);
}
