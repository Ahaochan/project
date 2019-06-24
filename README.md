# 简介
本项目是本人自用的一套 `Java` 代码库.

- ahao-all
  - [ahao-common-utils](https://github.com/Ahaochan/ahao-common-utils)(常用工具类模块)
  - [ahao-spring-boot-balance-datasources](./ahao-spring-boot-balance-datasources)(负载均衡的主从数据源)
  - [ahao-spring-boot-file-download](./ahao-spring-boot-file-download)(常用的文件下载实例, `word`、`excel`、`pdf`)
  - [ahao-spring-boot-mail](./ahao-spring-boot-mail)(简单的邮件客户端)
  - [ahao-spring-boot-mybatis-plus](./ahao-spring-boot-mybatis-plus)(简单集成`Mybatis Plus`)
  - [ahao-spring-boot-redis](./ahao-spring-boot-redis)(`Redis`方法级缓存和工具类)
  - [ahao-spring-boot-swagger](./ahao-spring-boot-swagger)(`Swagger`配置及使用)
  - [ahao-spring-boot-websocket](./ahao-spring-boot-websocket)(`Spring`提供的低层级`WebSocket`实现方式)
  - [ahao-spring-boot-websocket-endpoint](./ahao-spring-boot-websocket-endpoint)(`Java`提供的`WebSocket`实现方式)
  - [ahao-spring-boot-wechat](./ahao-spring-boot-wechat)(基于[`WxJava`](https://github.com/Wechat-Group/WxJava)的微信开发`Demo`)
  - [ahao-spring-cloud-eureka](./ahao-spring-cloud-eureka)(开箱即用的服务注册中心)
  - [ahao-web](./ahao-web) (常用业务代码)
  - ~~ahao-forum-guitar (论坛项目, 放弃维护)~~
  - ~~ahao-invoice (企业增值税数据分析系统, 放弃维护)~~

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

## Shiro 通用配置
1. 基于`Redis`的重试次数限制, 源码地址: [`RetryLimitHashedCredentialsMatcher`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/credential/RetryLimitHashedCredentialsMatcher.java)
2. `Shiro`用户登陆后会话标识未更新漏洞, 源码地址: [`LoginController`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/LoginController.java#L86-L114)

## 文件上传
`com.ahao.web.module.upload`

## 阿里银行数据接口
`com.ahao.web.module.alipay.bank`
