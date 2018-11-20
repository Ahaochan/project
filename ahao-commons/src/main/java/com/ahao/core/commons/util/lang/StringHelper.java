package com.ahao.core.commons.util.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author Ahaochan
 */
public class StringHelper {
    private static final String IGNORE_SEPARATOR_REGEX = "[\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\u0080-\\u00FF\\u2000-\\u206F\\u3000-\\u3020\\uFF00-\\uFFEF]{0,20}";
    /* 空字符串 */
    public static final String EMPTY_STRING = StringUtils.EMPTY;
    /* 点 */
    public static final char DOT = '.';
    /* 下划线 */
    public static final char UNDERSCORE = '_';
    /* 逗点及空格 */
    public static final String COMMA_SPACE = ", ";
    /* 逗点 */
    public static final String COMMA = ",";
    /* 开始括号 */
    public static final String OPEN_PAREN = "(";
    /* 结束括号 */
    public static final String CLOSE_PAREN = ")";
    /* 单引号 */
    public static final char SINGLE_QUOTE = '\'';

    public static final String CRLF = "\r\n";


    /**
     * 以 searchStrings 中任意一个元素结尾的字符串返回true, 忽略大小写
     *
     * @param string        字符串
     * @param searchStrings 待匹配字符串
     * @return 以 searchStrings 中任意一个元素结尾的字符串返回true
     */
    public static boolean endsWithAnyIgnoreCase(final CharSequence string, final CharSequence... searchStrings) {
        for (CharSequence search : searchStrings) {
            if (StringUtils.endsWithIgnoreCase(string, search)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除字符串首尾空格
     * 32 为 普通空格
     * 160 为 html的空格 &nbsp;
     * 12288 为 一个汉字宽度的空格
     *
     * @param str 待处理的字符串
     * @return 去除空格的字符串
     */
    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        char[] val = str.toCharArray();
        int len = val.length;
        int st = 0;

        while ((st < len) &&
                StringUtils.equalsAny(val[st] + "",
                        (char) (32) + "",
                        (char) (160) + "",
                        (char) (12288) + "")) {
            st++;
        }
        while ((st < len) &&
                StringUtils.equalsAny(val[len - 1] + "",
                        (char) (32) + "",
                        (char) (160) + "",
                        (char) (12288) + "")) {
            len--;
        }
        return ((st > 0) || (len < val.length)) ? str.substring(st, len) : str;
    }
}