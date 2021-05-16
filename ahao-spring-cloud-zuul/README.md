# 简介
一个`Zuul`网关的`Demo`. 将请求转发到其他主机上, 类似`Nginx`.

# 使用步骤
1. 启动[Eureka](../ahao-spring-cloud-eureka/ahao-spring-cloud-eureka-server/src/main/java/moe/ahao/spring/cloud/eureka/EurekaServerApplication.java)
1. 启动[Eureka-Provider](../ahao-spring-cloud-eureka/ahao-spring-cloud-eureka-provider/src/main/java/moe/ahao/spring/cloud/eureka/EurekaProviderApplication.java)
1. 启动[本项目](src/main/java/moe/ahao/spring/cloud/Starter.java)
1. 访问[http://127.0.0.1:8761/param?msg=123](http://127.0.0.1:8081/param?msg=123), 正常输出
1. 访问[http://127.0.0.1:8080/api/param?msg=123](http://127.0.0.1:8080/api/param?msg=123), 输出转发后的结果
