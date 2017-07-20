package com.ahao.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串工具类
 *
 * @author Ahaochan
 */
public final class StringHelper {
    private static final Logger logger = LoggerFactory.getLogger(StringHelper.class);
    private StringHelper(){

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
     * 把字符数组，转化为一个字符
     * @param seperator 字符分隔符
     * @param strings 数组对象
     * @return 字符串
     */
    public static String join(String seperator, String... strings) {
        return Stream.of(strings)
                        .collect(Collectors.joining(seperator));
    }

    /**
     * 把集合转化为一个字符串
     *
     * @param seperator 分隔符
     * @param objects   集合
     * @return 字符串
     */
    public static String join(String seperator, Collection<?> objects) {
        return objects.stream()
                .map(Object::toString)
                .collect(Collectors.joining(seperator));
    }

    /**
     * 如果字符串为空，则返回默认字符串
     *
     * @param source     源字符串
     * @param defaultStr 默认字符串
     * @return 转换后的字符串
     */
    public static String N2S(String source, String defaultStr) {
        return source != null ? source : defaultStr;
    }

    /**
     * 中文到Unicode
     */
    public static String chineseToUnicode(String chinese) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < chinese.length(); i++) {
            unicode.append("\\u").append(Integer.toHexString((int) chinese.charAt(i)));
        }
        return unicode.toString();
    }

    /**
     * 对URL的中文进行编码
     *
     * @param source 来源字符串
     * @return 编码后的字符串
     */
    @SuppressWarnings("deprecation")
    public static String URLEncode(String source) {
        return java.net.URLEncoder.encode(source);
    }

    /**
     * 对URL的中文进行解码
     *
     * @param source 来源字符串
     * @return 解码后的字符串
     */
    @SuppressWarnings("deprecation")
    public static String URLDecode(String source) {
        return java.net.URLDecoder.decode(source);
    }

    /**
     * 加密密码
     *
     * @param password 明文密码
     * @return String 加密后的密码
     */
    public static String buildPassword(String password) {
        // 后期需要更改成其他加密
        return DigestUtils.md5Hex(password);
    }

    /**
     * 生成UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }


    public static boolean isNumeric(String num){
        return StringUtils.isNumeric(num);
    }

    public static boolean isEmpty(String... str){
        return StringUtils.isAnyEmpty(str);
    }

    public static boolean isNotEmpty(String... str){
        return StringUtils.isNoneEmpty(str);
    }

    public static boolean endsWith(CharSequence sequence, CharSequence... searchStrings){
        return Stream.of(searchStrings)
                .anyMatch(s->StringUtils.endsWithIgnoreCase(sequence, s));

    }

}