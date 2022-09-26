package moe.ahao.operate.log.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.ifunc.IFunctionService;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * SpEL的模板
 */
@Data
@AllArgsConstructor
public class SpELTemplate implements IExpression {
    /**
     * SpEL表达式
     */
    private String spElExpression;

    @Override
    public String execute(ProceedingJoinPoint point, OperateLogExpressionEvaluator expressionEvaluator, IFunctionService iFunctionService) {
        return expressionEvaluator.executeStringExpression(point, spElExpression);
    }
}
