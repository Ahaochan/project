package moe.ahao.operate.log.evaluator;

import moe.ahao.operate.log.annotation.OperateLog;
import moe.ahao.operate.log.expression.IExpression;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 日志模板解析结果
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTemplateResult {

    /**
     * 日志模板里面是否定义表达式（函数表达式或者SpEL表达式）
     */
    private boolean hasExpression = false;

    /**
     * 表达式
     */
    private List<IExpression> expressions;

    /**
     * 操作日志模板
     */
    private String contentTemplate;

    /**
     * 业务no
     */
    private String bizNo;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 拦截链接点
     */
    private ProceedingJoinPoint point;

    /**
     * 操作日志注解
     */
    private OperateLog operateLog;

    public LogTemplateResult(boolean hasExpression) {
        this.hasExpression = hasExpression;
    }

    public LogTemplateResult(boolean hasExpression, List<IExpression> expressions, String contentTemplate) {
        this.hasExpression = hasExpression;
        this.expressions = expressions;
        this.contentTemplate = contentTemplate;
    }
}
