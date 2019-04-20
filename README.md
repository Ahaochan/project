# 简介
本项目是本人自用的一套 `Java` 代码库.

- ahao-all
  - ahoa-spring-cloud-eureka (服务注册中心 Demo)
  - ahao-web (Web项目通用代码)
  - ~~ahao-forum-guitar (论坛项目, 放弃维护)~~
  - ~~ahao-invoice (企业增值税数据分析系统, 放弃维护)~~
  
# 造核弹进度

## Spring Cloud Eureka
一个`Demo`, 提供了开箱即用的服务注册中心, 以及三种不同的客户端连接方式.
- `@LoadBalanced`修饰`RestTemplate`, 源码地址: [`L23-L41`](https://github.com/Ahaochan/project/blob/master/ahao-spring-cloud-eureka/src/main/java/com/ahao/spring/cloud/eureka/Client.java#L23-L41)
- 使用`LoadBalancerClient`获取**服务地址**, 再调用`RestTemplate`, 源码地址: [`L44-L60`](https://github.com/Ahaochan/project/blob/master/ahao-spring-cloud-eureka/src/main/java/com/ahao/spring/cloud/eureka/Client.java#L44-L60)
- 使用`Feign`框架做`RPC`, 源码地址: [`LL62-L78`](https://github.com/Ahaochan/project/blob/master/ahao-spring-cloud-eureka/src/main/java/com/ahao/spring/cloud/eureka/Client.java#L62-L78)

## Spring 异步任务配置
源码地址: [`com.ahao.spring.async`](https://github.com/Ahaochan/project/tree/master/ahao-web/src/main/java/com/ahao/spring/async)

先开启`@EnableAsync`注解.

写一个类继承自`AsyncConfigurerSupport`, 然后注册线程池`Bean`, `Spring Boot`会自动注入异步框架.

注册完成后, 就可以在方法上使用`@Async`注解, 标记为一个异步方法.

异步方法只能返回`void`或者`Feture<T>`.

## 负载均衡的主从数据源
源码地址: [`com.ahao.spring.jdbc`](https://github.com/Ahaochan/project/tree/master/ahao-web/src/main/java/com/ahao/spring/jdbc)

先在[`DataSourceConfiguration`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/spring/jdbc/datasource/DataSourceConfiguration.java)注册几个数据源`Bean`.

然后在[`LoadBalanceConfiguration`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/spring/jdbc/datasource/LoadBalanceConfiguration.java)注册真正的负载均衡数据源.

内置两种简单负载均衡算法

1. 随机
2. 顺序轮询

## Redis 方法级缓存 
源码地址: [`com.ahao.redis`](https://github.com/Ahaochan/project/tree/master/ahao-web/src/main/java/com/ahao/redis)

通过[`AOP`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/redis/aop/RedisCacheAOP.java)拦截[`@Redis`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/redis/annotation/Redis.java)注解.

默认以`包名.类名.方法名(参数1,参数2)`为`key`, 也可以使用`Spring EL`表达式作为`Key`.

1. 如果缓存命中, 则直接返回缓存
2. 如果缓存未命中, 则查询`DB`后, 存入缓存, 再返回结果

## Shiro 通用配置
1. 基于`Redis`的重试次数限制, 源码地址: [`RetryLimitHashedCredentialsMatcher`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/credential/RetryLimitHashedCredentialsMatcher.java)
2. `Shiro`用户登陆后会话标识未更新漏洞, 源码地址: [`LoginController`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/LoginController.java#L86-L114)

## 文件上传
`com.ahao.web.module.upload`

## 阿里银行数据接口
`com.ahao.web.module.alipay.bank`

# TODO
- docker化应用