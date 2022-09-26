package moe.ahao.operate.log.evaluator;

import lombok.Data;
import moe.ahao.operate.log.annotation.OperateLog;
import moe.ahao.operate.log.expression.IExpression;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 日志模板解析结果
 */
@Data
public class LogTemplateResult {

    /**
     * 日志模板里面是否定义表达式（函数表达式或者SpEL表达式）
     */
    private boolean hasExpression;

    /**
     * 表达式
     */
    private List<IExpression> expressions;

    /**
     * 操作日志模板
     * 已经清洗成包含%s的字符串, 使用expressions进行填充
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
