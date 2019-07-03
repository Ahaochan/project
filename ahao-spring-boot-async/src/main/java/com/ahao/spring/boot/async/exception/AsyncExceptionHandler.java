package com.ahao.spring.boot.async.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 异步任务发生异常的捕获策略
 */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        StringBuilder sb = new StringBuilder();

        // 1. 拼接类名
        Class clazz = method.getDeclaringClass();
        sb.append(clazz.getName());

        // 2. 拼接方法名
        sb.append('#').append(method.getName()).append('(');;

        // 3. 拼接参数
        Class[] parameterTypes = method.getParameterTypes();
        if(ArrayUtils.isNotEmpty(parameterTypes)) {
            for (int i = 0, len = parameterTypes.length; i < len; i++) {
                Class type = parameterTypes[i];
                sb.append(type.getName()).append(":").append(objects[i]).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(')');

        // 4. 拼接错误信息
        sb.append(", 错误信息: ").append(throwable.getMessage());

        System.out.println(sb.toString());
    }
}
