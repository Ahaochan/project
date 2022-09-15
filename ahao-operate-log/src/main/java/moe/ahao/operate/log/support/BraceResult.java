package moe.ahao.operate.log.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于寻找{}
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BraceResult {
    // "{"的下标
    private int leftBraceIndex;
    // "{"匹配的"}"的下标
    private int matchedRightBraceIndex;
    // "{"，"}"之间的内容
    private String betweenBraceContent;
}
