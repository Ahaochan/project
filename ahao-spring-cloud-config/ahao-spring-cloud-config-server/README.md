# 简介
开箱即用的`Config`配置中心, 

支持`Git`和`SVN`配置.
```properties
spring.cloud.config.server.git.uri = "https://github.com/spring-cloud-samples/config-repo"
spring.cloud.config.server.svn.uri = ""
```

访问配置文件
```text
/{文件名}-{环境}.yml
/{分支名}/{文件名}-{环境}.yml
```
比如我们访问`http://127.0.0.1:8887/foo-test.properties`.
这里的**文件名**就是`foo`, **环境**就是`test`.
