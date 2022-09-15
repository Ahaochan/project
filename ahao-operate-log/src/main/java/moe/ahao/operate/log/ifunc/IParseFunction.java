package moe.ahao.operate.log.ifunc;

/**
 * 自定义函数接口
 * @author zhonghuashishan
 * @version 1.0
 */
public interface IParseFunction {

    String functionName();

    String apply(Object value);
}
