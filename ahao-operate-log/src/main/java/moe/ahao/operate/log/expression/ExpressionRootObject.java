package moe.ahao.operate.log.expression;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SpEL表达式#root对应的对象
 */
@Data
@AllArgsConstructor
public class ExpressionRootObject {
    /**
     * 被@OperateLog拦截的业务方法所属类对象
     */
    private Object object;
    /**
     * 被@OperateLog拦截的业务方法的全部入参
     */
    private Object[] args;
}
