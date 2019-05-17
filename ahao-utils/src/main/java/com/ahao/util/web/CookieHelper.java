package com.ahao.util.web;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 */
public abstract class CookieHelper {
    private static final Logger logger = LoggerFactory.getLogger(CookieHelper.class);

    private CookieHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 向response中追加Cookie, 不能删除或覆盖, 要在客户端进行
     * @param name Cookie名称
     */
    public static void setCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        // 设置Maximum Age
        cookie.setMaxAge(1000);
        // 设置cookie路径为当前项目路径
//        cookie.setDomain("");
        cookie.setPath("/");
        // 添加cookie
        response.addCookie(cookie);
    }

    /**
     * 从request中取出Cookie
     * @param name Cookie名称
     */
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(name.equals(cookie.getName())){
                return cookie;
            }
        }
        return null;
    }

    /**
     * 从request中取出Cookie值
     * @param name         Cookie名称
     * @param defaultValue 默认值
     */
    public static String getString(String name, String defaultValue, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        return cookie == null ? defaultValue : cookie.getValue();
    }

    /**
     * 从request中取出Cookie值
     * @param name Cookie名称
     */
    @SuppressWarnings("unchecked")
    public static String getString(String name, HttpServletRequest request) {
        return getString(name, "", request);
    }

    /**
     * 从request中取出Cookie值, 转化为字符串数组
     * @param name           Cookie名称
     * @param separatorChars 分隔符
     */
    public static String[] getStringArray(String name, String separatorChars, HttpServletRequest request) {
        String value = getString(name, request);
        return StringUtils.split(value, separatorChars);
    }

    /**
     * 从request中取出Cookie值, 转化为int
     * @param name         Cookie名称
     * @param defaultValue 默认值
     */
    public static int getInt(String name, int defaultValue, HttpServletRequest request) {
        String value = getString(name, request);
        if (StringUtils.isEmpty(value) || !NumberUtils.isCreatable(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * 从request中取出Cookie值, 转化为int
     * @param name Cookie名称
     */
    public static int getInt(String name, HttpServletRequest request) {
        return getInt(name, 0, request);
    }

    /**
     * 从request中取出Cookie值, 转化为int数组
     * @param name           Cookie名称
     * @param separatorChars 分隔符
     */
    public static int[] getIntArray(String name, String separatorChars, HttpServletRequest request) {
        String[] array = getStringArray(name, separatorChars, request);
        if (ArrayUtils.isEmpty(array)) {
            return new int[0];
        }

        int len = array.length;
        int[] value = new int[len];
        for (int i = 0; i < len; i++) {
            if (NumberUtils.isCreatable(array[i])) {
                value[i] = Integer.parseInt(array[i]);
            } else {
                logger.error(name + "属性下标为[" + i + "]的值:" + array[i] + "不是int型");
            }
        }
        return value;
    }
}
