# 简介
一个`Demo`, 提供了开箱即用的`Zookeeper`安装脚本和`Zookeeper`客户端连接方式.

和`Eureka`的客户端调用方式差不多, 也可以用`Feign`来调用.

1. 安装`Zookeeper`, 这里有一个简单的安装脚本[`install.sh`](./script/install.sh)
1. 修改[`application.yml`](./src/main/resources/application.yml)中`Zookeeper`地址, 并启动[`Starter`](./src/main/java/moe/ahao/spring/cloud/Starter.java)
2. 修改[`application-test.yml`](./src/test/resources/application-test.yml)中`Zookeeper`地址, 执行单元测试[`ZookeeperTest`](./src/test/java/moe/ahao/spring/cloud/zookeeper/ZookeeperTest.java)
