package com.ahao.redis.aop;

import com.ahao.redis.annotation.Redis;
import com.ahao.redis.config.RedisKeys;
import com.ahao.redis.util.RedisHelper;
import com.ahao.spring.jdbc.DataSourceAOP;
import com.ahao.spring.util.SpelHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RedisCacheAOP {
    private static Logger logger = LoggerFactory.getLogger(DataSourceAOP.class);

    @Around("@annotation(redis)")
    public Object doAround(ProceedingJoinPoint pjp, Redis redis) throws Throwable {
        // 1. 根据切面参数, 获取 Redis 的 Key
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> returnType = method.getReturnType();

        String redisKey = RedisKeys.PREFIX_F + getKey(redis.key(), method, args);

        // 2. 如果穿透缓存, 则从 DB 获取并再存入 Redis, 以便下次命中
        boolean stab = redis.action() == Redis.DB;
        if (stab) {
            Object data = pjp.proceed();
            RedisHelper.set(redisKey, data); // TODO 异步存入 Redis
            return data;
        }

        // 3. 如果不穿透，从redis中获取数据
        Object resultFromRedis = RedisHelper.get(redisKey, returnType);
        if(resultFromRedis != null) {
            return resultFromRedis;
        }

        // 4. 如果缓存未命中, 则从 DB 获取并再存入 Redis, 以便下次命中
        Object data = pjp.proceed();
        RedisHelper.set(redisKey, data); // TODO 异步存入 Redis
        return data;
    }

    /**
     * 获取 Redis 的 key, 默认调用 {@link #getDefaultKey(Method, Object[])} 生成 key
     * @param SpEL   Spring EL 表达式
     * @param method 被拦截的方法
     * @param args   被拦截的方法的参数值
     * @return Redis 的 key
     */
    private String getKey(String SpEL, Method method, Object[] args) {
        // 1. 如果没有指定 key, 则默认以 包名.类名.方法名(参数1,参数2) 为 key
        if(StringUtils.isEmpty(SpEL)) {
            return getDefaultKey(method, args);
        }

        // 2. 获取被拦截方法的参数名
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        if(paraNameArr == null) {
            logger.debug("获取{}方法的参数名失败", method.getName());
            return getDefaultKey(method, args); // 获取参数失败, 则调用默认值
        }
        // 3. 转为 key-value 的形式
        Map<String, Object> argMap = new HashMap<>();
        for (int i = 0; i < paraNameArr.length; i++) {
            argMap.put(paraNameArr[i], args[i]);
        }
        // 4. 解析 SpEL 表达式
        return SpelHelper.parseString(SpEL, argMap);
    }

    /**
     * 反射生成默认的 Redis Key
     * @param method 被拦截的方法
     * @param args   被拦截的方法的参数值数组
     * @return 默认以 包名.类名.方法名(参数1,参数2) 为 key
     */
    private String getDefaultKey(Method method, Object[] args) {
        // 1. 获取被拦截的方法所属的类
        Class clazz = method.getDeclaringClass();
        StringBuilder defaultKey = new StringBuilder(clazz.getName());
        // 2. 拼接被拦截方法的方法名
        defaultKey.append('.').append(method.getName()).append('(');
        // 3. 拼接被拦截方法的参数值
        if(ArrayUtils.isNotEmpty(args)) {
            defaultKey.append(StringUtils.join(args, ","));
        }
        defaultKey.append(')');
        return defaultKey.toString();
    }
}
