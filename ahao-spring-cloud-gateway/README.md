# 简介
一个`Spring Cloud Gateway`网关的`Demo`. 将请求转发到其他主机上, 类似`Nginx`.
可以和[`Zuul`](../ahao-spring-cloud-zuul)进行对比.

# 使用步骤
1. 启动[Eureka](../ahao-spring-cloud-eureka/ahao-spring-cloud-eureka-server/src/main/java/moe/ahao/spring/cloud/eureka/EurekaServerApplication.java)
1. 启动[本项目](src/main/java/moe/ahao/spring/cloud/Starter.java)
1. 访问[http://127.0.0.1:8761/get](http://127.0.0.1:8761/get), 正常输出
1. 访问[http://127.0.0.1:8080/api/get](http://127.0.0.1:8080/api/get), 输出转发后的结果

# 参考资料
- [springcloud(十五)：服务网关 Spring Cloud GateWay 入门](http://www.ityouknow.com/springcloud/2018/12/12/spring-cloud-gateway-start.html)
- [springcloud(十六)：服务网关 Spring Cloud GateWay 服务化和过滤器](http://www.ityouknow.com/springcloud/2019/01/19/spring-cloud-gateway-service.html)
- [springcloud(十七)：服务网关 Spring Cloud GateWay 熔断、限流、重试](http://www.ityouknow.com/springcloud/2019/01/26/spring-cloud-gateway-limit.html)
