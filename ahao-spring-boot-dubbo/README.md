# 简介
一个`Demo`, 提供了简单的`Dubbo`脚手架环境.

1. 安装`Zookeeper`, 这里有一个简单的安装脚本[`install.sh`](../ahao-spring-cloud-zookeeper/script/install.sh)
1. 修改服务提供者[`application.yml`](./ahao-spring-boot-dubbo-provider/src/main/resources/application.yml)中`Zookeeper`地址, 并启动[`DubboProviderApplication`](./ahao-spring-boot-dubbo-provider/src/main/java/moe/ahao/spring/boot/DubboProviderApplication.java)
2. 修改服务消费者[`application.yml`](./ahao-spring-boot-dubbo-consumer/src/main/resources/application.yml)中`Zookeeper`地址, 执行单元测试[`SampleTest`](./ahao-spring-boot-dubbo-consumer/src/test/java/moe/ahao/spring/boot/dubbo/SampleTest.java)
