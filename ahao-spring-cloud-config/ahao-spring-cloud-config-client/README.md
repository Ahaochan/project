# 简介
开箱即用的`Config`配置中心的客户端.

1. 启动 [`ahao-spring-cloud-eureka`](../../ahao-spring-cloud-eureka) 注册中心
1. 启动 [`ahao-spring-cloud-config-server`](../ahao-spring-cloud-config-server) 配置中心
1. 启动 [`ahao-spring-cloud-config-client`](../ahao-spring-cloud-config-client) 客户端
1. 访问 [`http://127.0.0.1:8080/test`](http://127.0.0.1:8080/test), 获取远程配置中心的配置.
