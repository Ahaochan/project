# 简介
`Mybatis`代码生成器, 集成常用配置
使用方法
1. 修改 [`application-mysql.properties`](./src/main/resources/application-mysql.properties) 数据库连接配置.
1. 修改 [`mybatis-generator-config.xml`](./src/main/resources/mybatis-generator-config.xml), 填写要生成代码的表名和对象名.
1. 执行 `mvn org.mybatis.generator:mybatis-generator-maven-plugin:1.3.7:generate` 即可.
