package moe.ahao.unit.test.support;

import moe.ahao.unit.test.support.lambda.StdUtFunction;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;

import java.lang.invoke.SerializedLambda;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析lambda表达式的工具类
 */
public class SerializedLambdaUtil {
    private static final Map<Class<?>, SerializedLambda> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

    /**
     * 将Lambda表达式转化为属性名称
     */
    public static <T> String resolveProperty(StdUtFunction<T, ?> function) {
        SerializedLambda lambda = getLambda(function);
        if (Objects.isNull(lambda)) {
            return "";
        }
        // 保证方法是is、get、set开头
        String methodName = lambda.getImplMethodName();
        char[] charArr = methodName.toCharArray();
        int beginIndex;
        if (charArr.length >= 2 && charArr[0] == 'i' && charArr[1] == 's') {
            // is
            beginIndex = 2;
        } else if (charArr.length >= 3 && (charArr[0] == 'g' || charArr[0] == 's') && charArr[1] == 'e' && charArr[2] == 't') {
            // get or set
            beginIndex = 3;
        } else {
            throw new IllegalArgumentException("Error parsing property name '" + methodName + "'.  Didn't start with 'is', 'get' or 'set'.");
        }
        // 首字母转小写
        charArr[beginIndex] = Character.toLowerCase(charArr[beginIndex]);
        // 截取后面的字符
        char[] newCharArr = Arrays.copyOfRange(charArr, beginIndex, charArr.length);
        return new String(newCharArr);
    }


    /**
     * 解析 lambda 表达式
     *
     * @param func 需要解析的 lambda 对象
     * @param <T>  类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    private static <T> SerializedLambda getLambda(StdUtFunction<T, ?> func) {
        SerializedLambda lambda = CLASS_LAMBDA_CACHE.get(func.getClass());
        if (lambda == null) {
            try {
                lambda = ReflectHelper.executeMethod(func, "writeReplace");
                CLASS_LAMBDA_CACHE.put(func.getClass(), lambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lambda;
    }
}
