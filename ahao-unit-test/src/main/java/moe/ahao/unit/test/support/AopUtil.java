package moe.ahao.unit.test.support;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

/**
 * Aop代理工具类
 */
public class AopUtil {
    /**
     * 获取 目标对象
     * @param proxy 代理对象
     */
    public static Object getTarget(Object proxy) throws Exception {
        // 如果不是AOP代理对象, 就直接返回出去
        if(!org.springframework.aop.support.AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        // 如果是AOP代理对象, 并且是JDK动态代理
        if(org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        }
        // 如果是AOP代理对象, 并且是CGLIB动态代理
        else { // cglib
            return getCglibProxyTargetObject(proxy);
        }
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }
}
