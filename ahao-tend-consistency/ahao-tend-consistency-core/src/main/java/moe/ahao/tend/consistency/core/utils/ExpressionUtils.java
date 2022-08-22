package moe.ahao.tend.consistency.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * el表达式解析的工具类 用于解析配置中的表达式
 **/
@Slf4j
public class ExpressionUtils {

    private static final String START_MARK = "\\$\\{";

    public static final String RESULT_FLAG = "true";

    public static boolean parseBoolean(String expr, Map<String, Object> map) {
        String value = ExpressionUtils.parse(expr, map);
        return Boolean.parseBoolean(value);
    }

    public static String parse(String expr, Map<String, Object> map) {
        // 1. 转换表达式为SPEL表达式
        String newExpr = Optional.of(expr)
            .map(s -> StringUtils.replace(s, "executeTimes", "#taskInstance.executeTimes"))
            .map(s -> "${" + s + "}")
            .map(s -> s.replaceAll(START_MARK, "#{"))
            .orElse("");
        if (StringUtils.isBlank(expr)) {
            log.error("解析表达式{}时失败，newExpr:{}", expr, newExpr);
            return "";
    }
        log.info("改写后的表达式:{}", newExpr);

        try {
            // 2. 将map参数放到上下文, 给表达式读取
            // TODO StandardEvaluationContext是否可以作为全局变量
            EvaluationContext context = new StandardEvaluationContext();
            // 为了让表达式可以访问该对象, 先把对象放到上下文中
            map.forEach(context::setVariable);

            // 3. 解析表达式, 获取值
            // TODO SpelExpressionParser是否可以作为全局变量
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(newExpr, new TemplateParserContext());
            String value = expression.getValue(context, String.class);
            return value;
        } catch (Exception e) {
            log.error("解析表达式{}时，newExpr:{}，发生异常", expr, newExpr, e);
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
}
