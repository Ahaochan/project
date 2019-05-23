# 简介
通过继承`AbstractRoutingDataSource`重写`determineCurrentLookupKey`方法, 进行切换数据源操作.

```java
// https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/LoadBalanceConfiguration.java#L40-L48
AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isMaster = DataSourceContextHolder.isMaster();
        String key = loadBalance_polling(isMaster);
        logger.info(String.format("当前数据源切换到 %s 数据库: %s", isMaster ? "Master" : "Slave", key));
        return key;
    }
};
```

# 配置数据源
先在[`DataSourceConfiguration`](./ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/DataSourceConfiguration.java)注册几个数据源`Bean`.

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/DataSourceConfiguration.java#L14-L36

上面注册了三个数据源`Bean`, 并在[`yml`](./ahao-spring-boot-balance-datasources/src/main/resources/application.yml)配置文件`spring.datasource`配置数据源的参数.
并且指定主从数据源.

```yaml
# https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/resources/application.yml#L2-L11
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    master-bean-name: masterDataSource
    salve-bean-name: slave1DataSource,slave2DataSource

    master:
      username: root
      password: root
      url: jdbc:mysql://172.20.2.135:3306/my_db1?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
```

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
2. 顺序轮询(可以改造, 接入`Redis`计数)

```java
// https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-balance-datasources/src/main/java/com/ahao/spring/boot/datasources/datasource/LoadBalanceConfiguration.java#L62-L85
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
```