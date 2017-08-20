package com.ahao.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.stream.Stream;

/**
 * 字符串工具类
 *
 * @author Ahaochan
 */
public abstract class StringHelper {
    private static final Logger logger = LoggerFactory.getLogger(StringHelper.class);

    private StringHelper() {
    }

    private static final String IGNORE_SEPARATOR_REGEX = "[\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\u0080-\\u00FF\\u2000-\\u206F\\u3000-\\u3020\\uFF00-\\uFFEF]{0,20}";
    /* 空字符串 */
    public static final String EMPTY_STRING = "";
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
     * 将 byte[] 数组解码为UTF8的字符串
     * @param bytes 字节数组
     * @return UTF8编码的字符串
     */
    public static String toUTF8(byte[] bytes) {
        return StringUtils.toEncodedString(bytes, Charset.forName("UTF-8"));
    }

    /**
     * 判断字符串是否为数字
     * @param num 数字字符串
     * @return true为数字
     */
    public static boolean isNumeric(String num) {
        return StringUtils.isNumeric(num);
    }

    /**
     * 判断字符串数组中, 有一个为空则返回true
     *
     * @param str 字符串数组
     * @return 有一个为空则返回true
     */
    public static boolean isEmpty(String... str) {
        return StringUtils.isAnyEmpty(str);
    }

    /**
     * 判断字符串数组中, 有一个为空则返回false
     * @param str 字符串数组
     * @return 有一个为空则返回false
     */
    public static boolean isNotEmpty(String... str) {
        return StringUtils.isNoneEmpty(str);
    }

    /**
     * 判断 sequence 是否匹配 searchStrings 数组中任意一个元素为结尾字符串
     * @param sequence 字符串
     * @param searchStrings 匹配的字符串
     * @return 判断 sequence 是否匹配 searchStrings 数组中任意一个元素为结尾字符串
     */
    public static boolean endsWith(CharSequence sequence, CharSequence... searchStrings) {
        return Stream.of(searchStrings)
                .anyMatch(s -> StringUtils.endsWithIgnoreCase(sequence, s));
    }

    /**
     * 判断 sequence 是否与 searchStrings 数组中任意一个元素 相等
     * @param string 字符串
     * @param searchStrings 匹配的字符串
     * @return 判断 sequence 是否与 searchStrings 数组中任意一个元素 相等
     */
    public static boolean equals(String string, String... searchStrings) {
        return StringUtils.equalsAny(string, searchStrings);
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


    /**
     * 字符串 search 中包含任意一个字符 match, 则返回true
     * @param search 字符串
     * @param match 待匹配的字符
     * @return 包含任意一个字符 match, 则返回true
     */
    public static boolean containsAny(String search, int... match) {
        for (char c : search.toCharArray()) {
            if (equalsAny(c, match)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 如果 字符串 的 末尾 位置的元素为 ch, 则删除
     *
     * @param str 字符串
     * @param ch  待匹配的字符
     * @return 是否删除成功
     */
    public static boolean deleteStartIf(StringBuilder str, char ch) {
        return deleteIf(str, 0, ch);
    }

    /**
     * 如果 字符串 的 末尾 位置的元素为 ch, 则删除
     *
     * @param str 字符串
     * @param ch  待匹配的字符
     * @return 是否删除成功
     */
    public static boolean deleteEndIf(StringBuilder str, char ch) {
        return deleteIf(str, str.length() - 1, ch);
    }

    /**
     * 如果 字符串 的 index 位置的元素为 ch, 则删除
     *
     * @param str   字符串
     * @param index 位置
     * @param ch    待匹配的字符
     * @return 是否删除成功
     */
    public static boolean deleteIf(StringBuilder str, int index, char ch) {
        if (str.length() <= index) {
            return false;
        }
        if (str.charAt(index) == ch) {
            str.deleteCharAt(index);
            return true;
        }
        return false;
    }

    /**
     * 有一个 match 等于 search 则返回true
     *
     * @param search 原字符
     * @param match  待匹配的字符
     * @return 有一个 match 等于 search 则返回true
     */
    public static boolean equalsAny(char search, int... match) {
        for (int m : match) {
            if (search == m) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有一个 match 等于 search 则返回true
     *
     * @param string        原字符串
     * @param searchStrings 待匹配的字符串
     * @return 有一个 match 等于 search 则返回true
     */
    public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings) {
        for (CharSequence search : searchStrings) {
            if (StringUtils.equals(search, string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有一个 match 等于 search 则返回true
     *
     * @param string        原字符串
     * @param searchStrings 待匹配的字符串
     * @return 有一个 match 等于 search 则返回true
     */
    public static boolean equalsAnyIgnoreCase(final CharSequence string, final CharSequence... searchStrings) {
        for (CharSequence search : searchStrings) {
            if (StringUtils.equalsIgnoreCase(search, string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以 searchStrings 中任意一个元素开头的字符串返回true
     *
     * @param string        字符串
     * @param searchStrings 待匹配字符串
     * @return 以 searchStrings 中任意一个元素开头的字符串返回true
     */
    public static boolean startsWithAny(final CharSequence string, final CharSequence... searchStrings) {
        return StringUtils.startsWithAny(string, searchStrings);
    }

    /**
     * 以 searchStrings 中任意一个元素结尾的字符串返回true
     *
     * @param string        字符串
     * @param searchStrings 待匹配字符串
     * @return 以 searchStrings 中任意一个元素结尾的字符串返回true
     */
    public static boolean endsWithAny(final CharSequence string, final CharSequence... searchStrings) {
        return StringUtils.endsWithAny(string, searchStrings);
    }

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
}