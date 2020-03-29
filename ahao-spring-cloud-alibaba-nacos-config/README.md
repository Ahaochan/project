# 简介
`Nacos`是一个注册中心和配置中心。
这里只介绍配置中心使用。

# 使用
1. 安装`Nacos`, 这里提供了一个简单的安装脚本[`install-docker.sh`](./script/install-docker.sh)
1. 在[`bootstrap.yml`](./src/test/resources/bootstrap.yml)中填写`Nacos`配置.
1. 运行[单元测试](./src/test/java/moe/ahao/spring/cloud/alibaba/nacos/config/NacosConfigTest.java)

# 优先级
参考单元测试[`bootstrap.yml`](./src/test/resources/bootstrap.yml)的注释

# 参考文献
- [搭建生产可用的Nacos集群](http://www.itmuch.com/spring-cloud-alibaba/nacos-ha/)
