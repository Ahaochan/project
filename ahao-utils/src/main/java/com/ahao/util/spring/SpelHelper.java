package com.ahao.util.spring;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

public class SpelHelper {
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 使用 TemplateParserContext 解析 SpEL 表达式
     * @param expression SpEL 表达式
     * @param args       SpEL 表达式中的参数
     * @return 解析后的字符串
     */
    public static String parseString(String expression, Map<String, Object> args) {
        // 1. 将 参数 加入 Context
        if(args == null) {
            args = new HashMap<>(0);
        }
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(args);

        // 2. 替换参数
        Object returnVal = parser.parseExpression(expression, new TemplateParserContext())
                .getValue(context, Object.class);
        return returnVal == null ? "" : returnVal.toString();
    }
}
