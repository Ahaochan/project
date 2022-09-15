package moe.ahao.operate.log.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 校验花括号是否匹配的结果
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BraceValidResult {
    /**
     * 左括号的数量
     */
    private int leftBraceCount = 0;
    /**
     * 右括号的数量
     */
    private int rightBraceCount = 0;
    /**
     * 括号是否匹配：即左右括号是否能完全匹配
     */
    private boolean braceMatch = false;

    public BraceValidResult(boolean braceMatch) {
        this.braceMatch = braceMatch;
    }

    /**
     * 是否拥有{}
     * @return
     */
    public boolean hasBrace() {
        return braceMatch && leftBraceCount>=1 && rightBraceCount>=1;
    }
}
