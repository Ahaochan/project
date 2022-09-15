package moe.ahao.operate.log.aspect;

import moe.ahao.operate.log.annotation.OperateLog;
import moe.ahao.operate.log.evaluator.LogTemplateResult;
import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.expression.FuncInput;
import moe.ahao.operate.log.expression.FunctionTemplate;
import moe.ahao.operate.log.expression.IExpression;
import moe.ahao.operate.log.expression.SpELTemplate;
import moe.ahao.operate.log.ifunc.IFunctionService;
import moe.ahao.operate.log.model.OperateLogInstance;
import moe.ahao.operate.log.service.OperateLogStoreService;
import moe.ahao.operate.log.support.BraceResult;
import moe.ahao.operate.log.support.BraceUtils;
import moe.ahao.operate.log.support.BraceValidResult;
import moe.ahao.operate.log.support.MethodExecuteResult;
import lombok.extern.slf4j.Slf4j;
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

        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        LogTemplateResult logTemplateResult = null;

        try {
            // 1、解析操作日志模板和表达式
            logTemplateResult = parseOperateLog(point, operateLog);
        } catch (Exception e) {
            log.error("operate log parse before biz function exception", e);
        }

        try {
            // 2、执行业务方法
            ret = point.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }

        try {
            // 只有业务方法执行成功才构造和记录操作日志
            if (methodExecuteResult.isExecuteOk()) {
                // 3、执行表达式并填充日志模板占位符
                OperateLogInstance instance = executeExpressionAndFillPlaceholder(logTemplateResult);

                // 4、存储操作日志，需要业务方自行定义
                operateLogStoreService.storeOperateLog(instance);
            }
        } catch (Exception e) {
            // 构造和存储日志过程中错误不要影响业务
            log.error("operate log build and store exception", e);
        }

        // 4、抛出业务方法的exception
        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }

        return ret;
    }

    /**
     * 解析操作日志模板
     */
    private LogTemplateResult parseOperateLog(ProceedingJoinPoint point, OperateLog operateLog) {

        String operator = null;
        String bizNo = null;

        // 解析业务no
        bizNo = expressionEvaluator.executeStringExpression(point, operateLog.bizNo());
        if (StringUtils.isEmpty(bizNo)) {
            throw new UnsupportedOperationException("bizNo cannot be empty!!");
        }
        // 解析日志模板和表达式
        LogTemplateResult logTemplateResult = parseLogTemplateResult(operateLog.content());
        // 解析操作人
        operator = expressionEvaluator.executeStringExpression(point, operateLog.operator());

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
            List<String> expressionResult = new ArrayList<>();
            // 执行自定义函数
            for (IExpression expression : logTemplateResult.getExpressions()) {
                // 保存执行的结果
                expressionResult.add(expression.execute(point, expressionEvaluator, iFunctionService));
            }
            // 构造最终的日志content
            logContent = String.format(logContentTemplate, expressionResult.toArray(new String[expressionResult.size()]));
        } else {
            // 普通logContent
            logContent = operateLog.content();
        }

        // 构造操作日志实例
        return OperateLogInstance.builder()
                .bizNo(logTemplateResult.getBizNo())
                .operator(logTemplateResult.getOperator())
                .logContent(logContent)
                .build();
    }

    /**
     * 解析LogContent
     *
     * @param content
     * @return
     */
    private LogTemplateResult parseLogTemplateResult(String content) {
        BraceValidResult braceValidResult = BraceUtils.isBraceValid(content);
        // 如果没有{}或者左右括号不匹配，说明日志content里面不包含表达式
        if (!braceValidResult.hasBrace()) {
            return new LogTemplateResult(false);
        }
        List<IExpression> expressions = new ArrayList<>();
        String contentTemplate = "";
        // 找出所有的{}之间的内容，并输出contentTemplate
        // eg: content = "修改了订单的配送员：从【{queryOldUser{#request.getDeliveryOrderNo()}}】, 修改到【{deveryUser{#request.userId}}】";
        // out:
        //    queryOldUser{#request.getDeliveryOrderNo()}
        //    deveryUser{#request.userId}
        //    contentTemplate = 修改了订单的配送员：从【%s】, 修改到【%s】
        BraceResult braceResult = null;
        while (null != (braceResult = BraceUtils.findBraceResult(content))) {
            // 构造contentTemplate
            contentTemplate += content.substring(0, braceResult.getLeftBraceIndex());
            contentTemplate += "%s";
            // 截取剩余未处理完的字符串
            content = content.substring(braceResult.getMatchedRightBraceIndex() + 1);
            // 构造并添加IExpression
            expressions.add(parseExpression(braceResult.getBetweenBraceContent()));
        }
        contentTemplate += content;
        return new LogTemplateResult(true, expressions, contentTemplate);
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
    private IExpression parseExpression(String content) {
        BraceResult braceResult = BraceUtils.findBraceResult(content);
        if (null != braceResult) {
            // 自定义函数
            String functionName = content.substring(0, braceResult.getLeftBraceIndex());
            String input = content.substring(braceResult.getLeftBraceIndex() + 1, braceResult.getMatchedRightBraceIndex());
            return new FunctionTemplate(functionName, new FuncInput(input));
        } else {
            // SpEl表达式
            return new SpELTemplate(content);
        }
    }


}
