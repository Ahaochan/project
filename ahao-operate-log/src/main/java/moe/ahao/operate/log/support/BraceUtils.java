package moe.ahao.operate.log.support;

import java.util.Stack;

public class BraceUtils {
    /**
     * 校验左右花括号是否能一一匹配
     */
    public static BraceValidResult isBraceValid(String s) {
        if (s.length() <= 4) {
            return new BraceValidResult(false);
        }

        boolean braceMatch = false;
        int l = 0, r = 0;

        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c == '{') {
                stack.push(c);
                l++;
            } else if(c == '}') {
                if(stack.isEmpty()) {
                    break;
                }
                if (stack.pop() != '{') {
                    break;
                }
                r++;
            }
        }
        if (stack.isEmpty()) {
            braceMatch = true;
        }
        if(!braceMatch) {
            return new BraceValidResult(false);
        } else {
            return new BraceValidResult(true, l, r);
        }
    }

    /**
     * 查询content中的{}
     */
    public static BraceResult findBraceResult(String content) {
        // 找到第一个"{"
        int l = content.indexOf("{"), r = -1;
        if (l < 0) {
            return null;
        }
        // stack用于处理第一个"{"右边可能出现的"{"
        Stack<Character> stack = new Stack<>();
        stack.push('{');
        // 从第一个"{"开始遍历，找到其匹配的"}"的index
        for (int i = l + 1; i < content.length(); i++) {
            // 第一个"{"的右边还可能有"{"
            if (content.charAt(i) == '{') {
                stack.push('{');
            } else if (content.charAt(i) == '}') {
                // 遇到"}"，栈中的"{"就需要弹出一个
                stack.pop();
            }
            if (stack.isEmpty()) {
                // 当栈空了，说明我们要找的和第一个"{"匹配的"}"就找到了
                r = i;
                break;
            }
        }
        // 没有找到相匹配的"}"
        if (r == -1) {
            return null;
        }
        String betweenBraceContent = content.substring(l + 1, r);
        return new BraceResult(l, r, betweenBraceContent);
    }
}
