# Spring 异步任务
`Spring`异步任务是通过线程池和代理实现的.

使用步骤
1. 开启`@EnableAsync`注解.
1. 写一个类继承自`AsyncConfigurerSupport`, 然后注册线程池`Bean`, `Spring Boot`会自动注入异步框架.
1. 在方法上使用`@Async`注解, 标记为一个异步方法.

值得注意的是, 异步方法只能返回`void`或者`Feture<T>`, 对应`Runnable`和`Callable`.

具体使用可以查看[单元测试](./src/test/java).
