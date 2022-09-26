package moe.ahao.operate.log.support;

import lombok.Data;

/**
 * 校验花括号是否匹配的结果
 */
@Data
public class BraceValidResult {
    /**
     * 左括号和右括号是否能一一匹配
     */
    private boolean braceMatch = false;
    /**
     * 左括号的数量
     */
    private int leftBraceCount = 0;
    /**
     * 右括号的数量
     */
    private int rightBraceCount = 0;

    public BraceValidResult(boolean braceMatch) {
        this.braceMatch = braceMatch;
    }

    public BraceValidResult(boolean braceMatch, int leftBraceCount, int rightBraceCount) {
        this.braceMatch = braceMatch;
        this.leftBraceCount = leftBraceCount;
        this.rightBraceCount = rightBraceCount;
    }

    /**
     * 是否存在括号
     */
    public boolean hasBrace() {
        return braceMatch && leftBraceCount>=1 && rightBraceCount>=1;
    }
}
