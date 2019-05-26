# 简介
基于`spring-boot-starter-web-services`提供的`WebService`实现方式.

# 步骤
1. 新建[`student.xsd`](./src/main/resources/student.xsd)文件, 声明请求体, 响应体, 数据实体, 命名空间.
2. 在[`pom.xml`](./pom.xml)中添加[`jaxb2-maven-plugin`](http://www.mojohaus.org/jaxb2-maven-plugin)插件, 然后执行`mvn org.codehaus.mojo:jaxb2-maven-plugin:1.6:xjc`, 注意, 生成的代码和命名空间有关(`com.ahao.spring.boot.webservice.entity`).
3. 编写`Controller`层的[`StudentEndpoint`](./src/main/java/com/ahao/spring/boot/webservice/controller/StudentEndpoint.java), 并在[`WebServiceConfig`](./src/main/java/com/ahao/spring/boot/webservice/config/WebServiceConfig.java)注册相关信息.
4. 执行单元测试

# 槽点
`WebService`复杂的一比, 感觉应该是老古董级别的, 了解下就好了.