# 简介
一个`Demo`, 提供了开箱即用的服务注册中心, 以及三种不同的客户端连接方式.

客户端配置文件: [application-client.yml](./src/main/resources/application-client.yml)
单注册中心配置文件: [application-server.yml](./src/main/resources/application-server.yml)
多注册中心配置文件: [application-server-ha-1.yml](./src/main/resources/application-server-ha-1.yml)、[application-server-ha-2.yml](./src/main/resources/application-server-ha-2.yml)

- `@LoadBalanced`修饰`RestTemplate`, 源码地址: [`L23-L41`](./src/main/java/com/ahao/spring/cloud/eureka/Client.java#L23-L41)
- 使用`LoadBalancerClient`获取**服务地址**, 再调用`RestTemplate`, 源码地址: [`L44-L60`](./src/main/java/com/ahao/spring/cloud/eureka/Client.java#L44-L60)
- 使用`Feign`框架做`RPC`, 源码地址: [`LL62-L78`](./src/main/java/com/ahao/spring/cloud/eureka/Client.java#L62-L78)
