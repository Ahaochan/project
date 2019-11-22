# 简介
本项目是本人自用的一套 `Java` 代码库. 每个项目有自带的`Readme`, 点击即可阅读.

- 花点心思
  - [ahao-common-utils](https://github.com/Ahaochan/ahao-common-utils)(常用工具类模块)
  - [ahao-spring-boot-balance-datasources](./ahao-spring-boot-balance-datasources)(负载均衡的主从数据源)
  - [ahao-spring-boot-elastic-job](./ahao-spring-boot-elastic-job)(整合`Spring Boot`和`Elastic Job`分布式定时任务框架)
  - [ahao-spring-boot-jwt](./ahao-spring-boot-jwt)(整合`JWT`实现权限拦截)
  - [ahao-spring-boot-redis](./ahao-spring-boot-redis)(`Redis`方法级缓存和工具类)
  - [ahao-spring-boot-shiro](./ahao-spring-boot-shiro)(`Spring Boot`整合`Shiro`)
  - [ahao-spring-boot-wechat](./ahao-spring-boot-wechat)(基于[`WxJava`](https://github.com/Wechat-Group/WxJava)的微信开发`Demo`)
  - [ahao-web](./ahao-web) (常用业务代码)
  - ~~ahao-forum-guitar (论坛项目, 放弃维护)~~
  - ~~ahao-invoice (企业增值税数据分析系统, 放弃维护)~~
  
- Demo 级别
  - [ahao-spring-boot-async](./ahao-spring-boot-async)(`Spring`线程池异步任务)
  - [ahao-spring-boot-cache](./ahao-spring-boot-cache)(注解缓存框架)
  - [ahao-spring-boot-druid](./ahao-spring-boot-druid)(阿里数据源`Druid`整合)
  - [ahao-spring-boot-file-download](./ahao-spring-boot-file-download)(常用的文件下载实例, `word`、`excel`、`pdf`)
  - [ahao-spring-boot-jpa](./ahao-spring-boot-jpa)(简单的`Spring Boot JPA`整合)
  - [ahao-spring-boot-mail](./ahao-spring-boot-mail)(简单的邮件客户端)
  - [ahao-spring-boot-mybatis-plus](./ahao-spring-boot-mybatis-plus)(简单集成`Mybatis Plus`)
  - [ahao-spring-boot-okhttp3](./ahao-spring-boot-okhttp3)(简单集成`OkHttp3`)
  - [ahao-spring-boot-rabbitmq](./ahao-spring-boot-rabbitmq)(简单的`RabbitMQ`使用, 集成`fastjson`消息转换器)
  - [ahao-spring-boot-swagger](./ahao-spring-boot-swagger)(`Swagger`配置及使用)
  - [ahao-spring-boot-validator](./ahao-spring-boot-validator)(参数校验`JSR303/JSR-349`, 使用`AOP`统一处理校验错误)
  - [ahao-spring-boot-webservice](./ahao-spring-boot-webservice)(`Web Service`使用)
  - [ahao-spring-boot-webservice-cxf](./ahao-spring-boot-webservice-cxf)(`Web Service`使用, 基于`Apache CXF`)
  - [ahao-spring-boot-websocket](./ahao-spring-boot-websocket)(`Spring`提供的低层级`WebSocket`实现方式)
  - [ahao-spring-boot-websocket-endpoint](./ahao-spring-boot-websocket-endpoint)(`Java`提供的`WebSocket`实现方式)
  - [ahao-spring-cloud-hystrix](./ahao-spring-cloud-hystrix)(简单的`Hystrix`使用, 注意`Hystrix`已停更)
  - [ahao-spring-cloud-zookeeper](./ahao-spring-cloud-zookeeper)(`Zookeeper`服务注册中心, 客户端的简单使用)
  
- 开箱即用
  - [ahao-mybatis-generator](./ahao-mybatis-generator)(`Mybatis`代码生成器)
  - [ahao-spring-boot-admin-client](./ahao-spring-boot-admin-client)(基于`actuator`的监控系统客户端)
  - [ahao-spring-boot-admin-server](./ahao-spring-boot-admin-server)(基于`actuator`的监控系统服务端)
  - [ahao-spring-boot-apollo](./ahao-spring-boot-apollo)(基于[`Apollo`](https://github.com/ctripcorp/apollo)配置中心的客户端)
  - [ahao-spring-boot-actuator](./ahao-spring-boot-actuator)(对`Spring`应用的监控)
  - [ahao-spring-cloud-config-server](./ahao-spring-cloud-config-server)(`Config`统一配置中心客户端)
  - [ahao-spring-cloud-config-client](./ahao-spring-cloud-config-client)(`Config`统一配置中心)
  - [ahao-spring-cloud-eureka](./ahao-spring-cloud-eureka)(`Eureka`服务注册中心)
  - [ahao-spring-cloud-gateway](./ahao-spring-cloud-gateway)(`Gateway`网关)
  - [ahao-spring-cloud-zuul](./ahao-spring-cloud-zuul)(`Zuul`网关)

## Shiro 通用配置
1. 基于`Redis`的重试次数限制, 源码地址: [`RetryLimitHashedCredentialsMatcher`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/credential/RetryLimitHashedCredentialsMatcher.java)
2. `Shiro`用户登陆后会话标识未更新漏洞, 源码地址: [`LoginController`](https://github.com/Ahaochan/project/blob/master/ahao-web/src/main/java/com/ahao/rbac/shiro/LoginController.java#L86-L114)

## 文件上传
`com.ahao.web.module.upload`

## 阿里银行数据接口
`com.ahao.web.module.alipay.bank`
