package moe.ahao.operate.log.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义函数入参
 */
@Getter
@AllArgsConstructor
public class FunInput {
    private final String args;

    /**
     * 规定以#开头的是SpEL表达式
     */
    public Boolean isSpEl() {
        return args.startsWith("#");
    }
}
