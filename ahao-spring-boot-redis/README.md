# 简介
本模块提供了
1. 基于注解的方法级`Redis`缓存
1. 隐性依赖`Spring`的`RedisHelper`工具类, 提供静态方法.

# 配置
核心的配置都由`spring-data-redis`提供了, 这里没做什么配置.
通过[`AOP`](./ahao-spring-boot-redis/src/main/java/com/ahao/spring/boot/redis/aop/RedisCacheAOP.java)拦截

1. 如果缓存命中, 则直接返回缓存
1. 如果缓存未命中, 则查询`DB`后, 存入缓存, 再返回结果

# 使用
只要配置好`Redis`地址, 然后使用[`@Redis`](./ahao-spring-boot-redis/src/main/java/com/ahao/spring/boot/redis/annotation/Redis.java)注解修饰要做缓存的方法即可.

https://github.com/Ahaochan/project/blob/master/ahao-spring-boot-redis/src/main/java/com/ahao/spring/boot/redis/annotation/Redis.java#L8-L15

1. 支持缓存穿透
1. 支持自定义`Key`, 默认使用`包名.类名(参数1,参数2...)`作为`key`, 支持`Spel表达式`
1. 强制缓存时间, 默认缓存`1`天.

# SpEL表达式

https://github.com/Ahaochan/project/blob/master/ahao-utils/src/test/java/com/ahao/util/spring/SpelHelperTest.java#L12-L20

可以将参数名, 填入`SpEL`表达式, 会自动将参数值注入, 作为`Redis Key`