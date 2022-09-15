package moe.ahao.operate.log.expression;

import lombok.Getter;

/**
 * 自定义函数入参
 * @author zhonghuashishan
 * @version 1.0
 */
@Getter
public class FuncInput {

    private final String input;

    public FuncInput(String input) {
        this.input = input;
    }

    /**
     * 规定以#开头的是SpEL表达式
     * @return
     */
    public Boolean isSpEl() {
        return input.startsWith("#");
    }
}
