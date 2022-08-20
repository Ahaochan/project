package com.ruyuan.consistency.utils;

import com.ruyuan.consistency.model.ConsistencyTaskInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * el表达式解析的工具类 用于解析配置中的表达式
 *
 * @author zhonghuashishan
 **/
@Slf4j
public class ExpressionUtils {

    private static final String START_MARK = "\\$\\{";

    public static final String RESULT_FLAG = "true";

    /**
     * 重写表达式
     *
     * @param alertExpression 告警表达式
     * @return 告警表达式
     */
    public static String rewriteExpr(String alertExpression) {
        String exprExpr = StringUtils.replace(alertExpression, "executeTimes", "#taskInstance.executeTimes");
        StringJoiner exprJoiner = new StringJoiner("", "${", "}");
        exprJoiner.add(exprExpr);
        return exprJoiner.toString();
    }

    /**
     * 获取指定EL表达式在指定对象中的值
     *
     * @param expr    spring el表达式
     * @param dataMap 数据集合 ref -> data 对象引用 -> data
     * @return 结果
     */
    public static String readExpr(String expr, Map<String, Object> dataMap) {
        try {
            expr = formatExpr(expr);
            // 表达式的上下文,
            EvaluationContext context = new StandardEvaluationContext();
            // 为了让表达式可以访问该对象, 先把对象放到上下文中
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                // key -> ref   value -> iterator.next().getValue()
                context.setVariable(entry.getKey(), entry.getValue());
            }
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(expr, new TemplateParserContext());
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            log.error("解析表达式{}时，发生异常", expr, e);
            return "";
        }
    }

    /**
     * 构造数据map
     *
     * @param object 要访问的数据模型
     * @return 数据map
     */
    public static Map<String, Object> buildDataMap(Object object) {
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("taskInstance", object);
        return dataMap;
    }

    /**
     * 对表达式进行格式化 ${xxx.name} -> #{xxx.name}
     *
     * @param expr 表达式
     */
    private static String formatExpr(String expr) {
        return expr.replaceAll(START_MARK, "#{");
    }

    public static void main(String[] args) {
        ConsistencyTaskInstance instance = ConsistencyTaskInstance.builder()
                .executeTimes(4)
                .build();
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("taskInstance", instance);

        String expr = "executeTimes > 1 && executeTimes < 5";
        String executeTimesExpr = StringUtils.replace(expr, "executeTimes", "#taskInstance.executeTimes");
        System.out.println(executeTimesExpr);
        System.out.println(readExpr("${" + executeTimesExpr + "}", dataMap));

        String expr2 = "executeTimes % 2 == 0";
        String executeTimesExpr2 = StringUtils.replace(expr2, "executeTimes", "#taskInstance.executeTimes");
        System.out.println(executeTimesExpr2);
        System.out.println(readExpr("${" + executeTimesExpr2 + "}", dataMap));

        System.out.println(readExpr(rewriteExpr(expr), dataMap));
    }

}
