package moe.ahao.operate.log.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.ifunc.IFunctionService;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 自定义function的模板
 */
@Data
@AllArgsConstructor
public class FunctionTemplate implements IExpression {
    /**
     * 自定义函数名称
     */
    private String functionName;
    /**
     * 自定义函数入参
     */
    private FunInput input;

    @Override
    public String execute(ProceedingJoinPoint point,
                          OperateLogExpressionEvaluator expressionEvaluator,
                          IFunctionService iFunctionService) {

        Object value = input.isSpEl() ?
            expressionEvaluator.executeObjectExpression(point, input.getArgs()) :
            input.getArgs();
        return iFunctionService.apply(functionName, value);
    }
}
