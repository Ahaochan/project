package moe.ahao.operate.log.evaluator;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 操作日志的
 * @author zhonghuashishan
 * @version 1.0
 */
public class OperateLogEvaluationContext extends MethodBasedEvaluationContext {

    public OperateLogEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                       ParameterNameDiscoverer parameterNameDiscoverer) {
        // 把方法的参数都放到 SpEL 解析的 RootObject 中
        super(rootObject, method, arguments, parameterNameDiscoverer);
    }
}
