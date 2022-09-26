package moe.ahao.operate.log.evaluator;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 操作日志的
 */
public class OperateLogEvaluationContext extends MethodBasedEvaluationContext {
    public OperateLogEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer
                                       // Object ret, String errorMsg
    ) {
        // 把方法的参数都放到 SpEL 解析的 RootObject 中
        super(rootObject, method, arguments, parameterNameDiscoverer);

        // 把 LogRecordContext 中的变量都放到 RootObject 中
        // Map<String, Object> variables = LogRecordContext.getVariables();
        // if (variables != null && variables.size() > 0) {
        //     this.setVariables(variables);
        // }

        // 把方法的返回值和 ErrorMsg 都放到 RootObject 中
        // this.setVariable("_ret", ret);
        // this.setVariable("_errorMsg", errorMsg);
    }
}
