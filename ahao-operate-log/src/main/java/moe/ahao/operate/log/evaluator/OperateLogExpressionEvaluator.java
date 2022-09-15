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

    /**
     * Create the suitable {@link EvaluationContext} for the specified event handling
     * on the specified method.
     */
    public OperateLogEvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(object, args);
        return new OperateLogEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
    }

    public String executeStringExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        return executeExpression(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition,String.class);
    }

    public Object executeObjectExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        return executeExpression(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition,Object.class);
    }

    private <T> T executeExpression(Object object, Object[] args, Class clazz, Method method, String condition, Class<T> vClazz) {
        if (args == null) {
            return null;
        }
        EvaluationContext evaluationContext = createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return executeExpression(condition, methodKey, evaluationContext,vClazz);
    }

    /**
     * 执行SpEL表达式的核心方法
     */
    public <T> T executeExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext, Class<T> vClazz) {
        return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, vClazz);
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            if (targetMethod == null) {
                targetMethod = method;
            }
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
