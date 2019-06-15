# 特性
1. 支持多数据源、读写分离、分组数据源
1. 通过 `yml` 配置, 即可自动添加数据源
1. 提供轮询、随机两种负载均衡算法
1. 支持注解形式的切换数据源方式

# 使用
在`yml`配置文件`spring.datasource.dynamic`添加`datasource`属性, 配置数据源即可.
```yaml
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master_1:
          url: jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db1.sql';
        slave_1:
          url: jdbc:h2:mem:db2;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db2.sql';
        slave_2:
          url: jdbc:h2:mem:db3;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:init-db3.sql';
```
如果是分组数据源, 只要用`_`分割组名即可, 比如`slave`.

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
