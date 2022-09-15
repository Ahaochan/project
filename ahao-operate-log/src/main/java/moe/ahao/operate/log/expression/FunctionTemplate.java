package moe.ahao.operate.log.expression;

import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.ifunc.IFunctionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 自定义function的模板
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionTemplate implements IExpression {

    /**
     * 自定义函数名称
     */
    private String functionName;

    /**
     * 自定义函数入参
     */
    private FuncInput input;

    @Override
    public String execute(ProceedingJoinPoint point,
                          OperateLogExpressionEvaluator expressionEvaluator,
                          IFunctionService iFunctionService) {

        Object value = null;
        if(input.isSpEl()) {
            //执行SpEL
            value = expressionEvaluator.executeObjectExpression(point, input.getInput());
        }
        else {
            value = input.getInput();
        }
        return iFunctionService.apply(functionName,value);
    }
}
