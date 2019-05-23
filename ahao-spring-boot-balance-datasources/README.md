# 简介
通过继承`AbstractRoutingDataSource`重写`determineCurrentLookupKey`方法, 进行切换数据源操作.

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/LoadBalanceConfiguration.java#L40-L48

# 配置数据源
先在[`DataSourceConfiguration`](./ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/DataSourceConfiguration.java)注册几个数据源`Bean`.

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/DataSourceConfiguration.java#L14-L36

上面注册了三个数据源`Bean`, 并在[`yml`](./ahao-spring-boot-balance-datasources/src/main/resources/application.yml)配置文件`spring.datasource`配置数据源的参数.
并且指定主从数据源.

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/resources/application.yml#L2-L11

# 使用
只要在方法上面加上注解即可.
主数据源: `@MasterDataSource`
从数据源: `@SlaveDataSource`

如果要手动切换, 调用以下代码即可, 和注解是等价的.
主数据源: `DataSourceContextHolder.master();`
从数据源: `DataSourceContextHolder.slave();`
清除配置: `DataSourceContextHolder.clear();`避免影响后续数据源切换.


# 负载均衡算法
内置两种简单负载均衡算法
1. 随机
2. 顺序轮询(可以改造接入`Redis`计数)

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/LoadBalanceConfiguration.java#L62-L85