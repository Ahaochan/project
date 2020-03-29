# 特性
1. 支持多数据源、读写分离、分组数据源
1. 通过 [`DataSourcePropertiesRepository`](./src/main/java/com/ahao/spring/boot/datasources/repository/DataSourcePropertiesRepository.java) 配置, 从不同渠道加载数据源
1. 提供轮询、随机两种负载均衡算法
1. 支持注解、`Filter`形式的切换数据源方式

# 使用
第一步: 在`yml`配置文件`spring.datasource.balance`配置相关参数.
```yaml
spring:
  datasource:
    balance:
      primary: master
      load-balance-strategy: com.ahao.spring.boot.datasources.strategy.PollingStrategy
      group-by: "_"
```
第二步: 实现[`DataSourcePropertiesRepository`](./src/main/java/com/ahao/spring/boot/datasources/repository/DataSourcePropertiesRepository.java)接口并注册为`Bean`, 提供数据源参数. 具体可以参考[`DataSourcePropertiesMemoryImpl`](./src/main/java/com/ahao/spring/boot/datasources/repository/DataSourcePropertiesMemoryImpl.java).
第三步: 选择[`DataSourceAOP`](./src/main/java/com/ahao/spring/boot/datasources/aop/DataSourceAOP.java)或[`DataSourceFilter`](./src/main/java/com/ahao/spring/boot/datasources/aop/DataSourceFilter.java)注册为`Bean`, 也可以两个都用.

如果是分组数据源, 只要用`_`分割组名即可, 比如`slave_1`.

然后在方法上面加上注解即可.
主数据源: `@MasterDataSource`
从数据源: `@SlaveDataSource`
自定义数据源: `@DataSource("数据源名称")`

如果要手动切换, 调用以下代码即可, 和注解是等价的.
主数据源: `DataSourceContextHolder.set("master");`
从数据源: `DataSourceContextHolder.set("slave");`
清除配置: `DataSourceContextHolder.clear();`避免影响后续数据源切换.

# 负载均衡算法
内置两种简单负载均衡算法, 参考[`strategy`包](./src/main/java/com/ahao/spring/boot/datasources/strategy).
1. 随机
2. 顺序轮询(可以改造, 接入`Redis`计数)
