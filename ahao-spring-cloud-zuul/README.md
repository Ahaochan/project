# 简介
一个`Zuul`网关的`Demo`. 将请求转发到其他主机上, 类似`Nginx`.

# 使用步骤
1. 启动[Eureka](../ahao-spring-cloud-eureka/src/main/java/com/ahao/spring/cloud/eureka/EurekaApplication.java)
1. 启动[本项目](./src/main/java/com/ahao/spring/cloud/Starter.java)
1. 访问[http://127.0.0.1:8761/get](http://127.0.0.1:8761/get), 正常输出
1. 访问[http://127.0.0.1:8080/api/get](http://127.0.0.1:8080/api/get), 输出转发后的结果
