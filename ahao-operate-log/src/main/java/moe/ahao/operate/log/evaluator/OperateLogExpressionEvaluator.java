package moe.ahao.operate.log.evaluator;

import moe.ahao.operate.log.expression.ExpressionRootObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志的SpEL Evaluator
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class OperateLogExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * shared param discoverer since it caches data internally
     */
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String executeStringExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        Class<String> clazz = String.class;
        return this.executeExpression(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
            ((MethodSignature) joinPoint.getSignature()).getMethod(), condition, clazz);
    }

    public Object executeObjectExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        Class<Object> clazz = Object.class;
        return this.executeExpression(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
            ((MethodSignature) joinPoint.getSignature()).getMethod(), condition, clazz);
    }

    /**
     * @param object    切入点的目标方法所在的对象
     * @param args      切入点的目标方法的参数
     * @param clazz     切入点的目标方法所在的对象的class类
     * @param method    切入点的目标方法
     * @param condition Spring EL表达式
     * @param vClazz    将解析后的值转为这个Class类型
     * @return
     */
    private <T> T executeExpression(Object object, Object[] args, Class<?> clazz, Method method, String condition, Class<T> vClazz) {
        if (args == null) {
            return null;
        }
        // 把AOP拦截的类、class、method、入参, 封装成一个表达式解析的上下文EvaluationContext
        EvaluationContext evaluationContext = this.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return this.executeExpression(condition, methodKey, evaluationContext, vClazz);
    }

    /**
     * Create the suitable {@link EvaluationContext} for the specified event handling
     * on the specified method.
     */
    private OperateLogEvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {
        Method targetMethod = this.getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(object, args);
        return new OperateLogEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
    }

    /**
     * 获取目标类的目标方法, 如果被代理了就取出被代理类的目标方法
     * 并存入缓存中, 下次直接从缓存中获取
     *
     * @param targetClass 目标类, 可能被代理了
     * @param method      目标方法
     * @return 目标方法
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

    /**
     * 执行SpEL表达式的核心方法
     */
    private <T> T executeExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext, Class<T> vClazz) {
        Expression expression = super.getExpression(this.expressionCache, methodKey, conditionExpression);
        return expression.getValue(evalContext, vClazz);
    }
}
