package moe.ahao.operate.log.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SpEL表达式#root对应的对象
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
