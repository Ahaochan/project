package moe.ahao.operate.log.aspect;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.operate.log.annotation.OperateLog;
import moe.ahao.operate.log.evaluator.LogTemplateResult;
import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.expression.FunInput;
import moe.ahao.operate.log.expression.FunctionTemplate;
import moe.ahao.operate.log.expression.IExpression;
import moe.ahao.operate.log.expression.SpELTemplate;
import moe.ahao.operate.log.ifunc.IFunctionService;
import moe.ahao.operate.log.model.OperateLogInstance;
import moe.ahao.operate.log.service.OperateLogStoreService;
import moe.ahao.operate.log.support.*;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志切面
 */
@Slf4j
@Component
@Aspect
public class OperateLogAspect {
    @Autowired
    private OperateLogExpressionEvaluator expressionEvaluator;
    @Autowired
    private IFunctionService iFunctionService;
    @Autowired
    private OperateLogStoreService operateLogStoreService;

    /**
     * 标注了LogRecord的注解的方法执行前要做的工作
     *
     * @param point 切面信息
     */
    @Around("@annotation(operateLog)")
    public Object markOperateLog(ProceedingJoinPoint point, OperateLog operateLog) throws Throwable {
        // 1. 执行业务代码
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        try {
            ret = point.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }

        // 2. 解析操作日志模板和表达式
        LogTemplateResult logTemplateResult = null;
        try {
            logTemplateResult = this.parseOperateLog(point, operateLog);
        } catch (Exception e) {
            log.error("operate log parse before biz function exception", e);
        }

        try {
            // 只有业务方法执行成功才构造和记录操作日志
            if (logTemplateResult != null && methodExecuteResult.isExecuteOk()) {
                // 3、执行表达式并填充日志模板占位符
                OperateLogInstance instance = this.executeExpressionAndFillPlaceholder(logTemplateResult);
                // 4、存储操作日志，需要业务方自行定义
                operateLogStoreService.storeOperateLog(instance);
            }
        } catch (Exception e) {
            log.error("operate log build and store exception", e);
        }

        // 4. 抛出业务方法的exception
        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }
        return ret;
    }

    /**
     * 解析操作日志模板
     */
    private LogTemplateResult parseOperateLog(ProceedingJoinPoint point, OperateLog operateLog) {
        // 1. 解析业务编码
        String bizNo = expressionEvaluator.executeStringExpression(point, operateLog.bizNo());
        if (StringUtils.isEmpty(bizNo)) {
            throw new UnsupportedOperationException("bizNo cannot be empty!!");
        }
        // 2. 解析操作人
        String operator = expressionEvaluator.executeStringExpression(point, operateLog.operator());

        // 3. 解析日志模板和表达式
        LogTemplateResult logTemplateResult = this.parseLogTemplateResult(operateLog.success());
        logTemplateResult.setOperateLog(operateLog);
        logTemplateResult.setBizNo(bizNo);
        logTemplateResult.setOperator(operator);
        logTemplateResult.setPoint(point);
        return logTemplateResult;
    }


    /**
     * 执行表达式并填充日志模板占位符
     */
    private OperateLogInstance executeExpressionAndFillPlaceholder(LogTemplateResult logTemplateResult) {
        String logContent = null;
        ProceedingJoinPoint point = logTemplateResult.getPoint();
        OperateLog operateLog = logTemplateResult.getOperateLog();

        // 如果日志LogContentTemplate里面包含表达式的话
        if (logTemplateResult.isHasExpression()) {
            String logContentTemplate = logTemplateResult.getContentTemplate();
            List<String> expressionResults = new ArrayList<>();
            // 执行自定义函数
            for (IExpression expression : logTemplateResult.getExpressions()) {
                // 保存执行的结果
                String expressionResult = expression.execute(point, expressionEvaluator, iFunctionService);
                expressionResults.add(expressionResult);
            }
            // 构造最终的日志content
            logContent = String.format(logContentTemplate, (Object[]) expressionResults.toArray(new String[0]));
        } else {
            // 普通logContent
            logContent = operateLog.success();
        }

        // 构造操作日志实例
        OperateLogInstance operateLogInstance = new OperateLogInstance();
        operateLogInstance.setBizNo(logTemplateResult.getBizNo());
        operateLogInstance.setOperator(logTemplateResult.getOperator());
        operateLogInstance.setLogContent(logContent);
        return operateLogInstance;
    }

    /**
     * 解析LogContent
     */
    private LogTemplateResult parseLogTemplateResult(String content) {
        // 1. 如果没有{}或者左右括号不匹配，说明日志content里面不包含表达式
        BraceValidResult braceValidResult = BraceUtils.isBraceValid(content);
        if (!braceValidResult.hasBrace()) {
            return new LogTemplateResult(false);
        }
        // 2. 找出所有的{}之间的内容，并输出contentTemplate
        // eg: content = "修改了订单的配送员：从【{queryOldUser{#request.getDeliveryOrderNo()}}】, 修改到【{deveryUser{#request.userId}}】";
        // out:
        //    queryOldUser{#request.getDeliveryOrderNo()}
        //    deveryUser{#request.userId}
        //    contentTemplate = 修改了订单的配送员：从【%s】, 修改到【%s】
        StringBuilder contentTemplate = new StringBuilder();
        List<IExpression> expressions = new ArrayList<>();
        BraceResult braceResult;
        while ((braceResult = BraceUtils.findBraceResult(content)) != null) {
            // 2.1. 遍历每一个{}, 将括号内的字符串替换为%s
            contentTemplate.append(content, 0, braceResult.getLeftBraceIndex()).append("%s");
            // 2.2. 将括号内的字符串进行表达式解析
            expressions.add(this.parseExpressionInBrace(braceResult.getBetweenBraceContent()));
            // 2.3. 截取剩余未处理完的字符串, 下个循环继续处理, 找到下一个{}
            content = content.substring(braceResult.getRightBraceIndex() + 1);
        }
        // 2.4. 拼接最后剩下的字符串
        contentTemplate.append(content);
        return new LogTemplateResult(true, expressions, contentTemplate.toString());
    }

    /**
     * 从funcContent解析出自定义函数template
     * <p>
     * 1、自定义函数
     * eg1: content = queryOldUser{#request.getDeliveryOrderNo()}
     * out:
     * functionName = queryOldUser
     * functionInput = #request.getDeliveryOrderNo()
     * 2、SpEl表达式
     * eg2: content = #request.userName
     * out:
     * spElExpression = #request.userName
     *
     * @param content
     * @return
     */
    private IExpression parseExpressionInBrace(String content) {
        // 1. 找到content里的{}
        BraceResult braceResult = BraceUtils.findBraceResult(content);
        // 2. 如果当前表达式里存在{}, 就说明是一个函数表达式
        if (null != braceResult) {
            // 2.1. 获取函数名
            String functionName = content.substring(0, braceResult.getLeftBraceIndex());
            // 2.2. 获取函数入参
            String args = content.substring(braceResult.getLeftBraceIndex() + 1, braceResult.getRightBraceIndex());
            return new FunctionTemplate(functionName, new FunInput(args));
        }
        // 3. 如果当前表达式不存在{}, 就说明只是个简单的SpEL表达式
        else {
            return new SpELTemplate(content);
        }
    }
}
