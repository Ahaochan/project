# 简介
`Spring`提供的一套缓存框架.
支持`Memory`、`EhCache`、`Redis`等缓存.
只需要配置`spring.cache.type`并引入相关依赖即可.
具体使用可以参考[CacheTest.java](./src/test/java/moe/ahao/spring/boot/cache/CacheTest.java)

之前我还写了个`AOP`实现的`Redis`缓存轮子, 结果已经有现成的了. 就直接拿来用吧.
轮子就在[ahao-spring-boot-redis](../ahao-spring-boot-redis)里.
