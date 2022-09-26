package moe.ahao.operate.log.expression;

import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.ifunc.IFunctionService;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 表达式
 */
public interface IExpression {

    /**
     * 执行表达式
     * @param point
     * @param expressionEvaluator
     * @param iFunctionService
     * @return
     */
    String execute(ProceedingJoinPoint point,
                   OperateLogExpressionEvaluator expressionEvaluator,
                   IFunctionService iFunctionService);

}
