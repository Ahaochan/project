package com.ahao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class RequestHelper {
    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    private RequestHelper() {

    }

    // ----------------------- 设置 Attribute 属性------------------------------
    /**
     * 向request中设置Attribute字符串
     *
     * @param key     名称
     * @param value   值
     * @param request request
     */
    public static <T> void setAttr(String key, T value, ServletRequest request) {
        if (StringHelper.isEmpty(key)) {
            logger.debug("key:" + key + "为空, 未向request存入" + value);
            return;
        }
        request.setAttribute(key, value);
    }

    /**
     * 从request中取出Attribute值
     *
     * @param key          名称
     * @param defaultValue 取值失败的默认值
     * @param request      request
     * @param <T>          泛型
     * @return 取值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttr(String key, T defaultValue, ServletRequest request) {
        Object value = request.getAttribute(key);
        if (value instanceof String) {
            value = String.valueOf(value).trim();
        }
        return value == null ? defaultValue : (T) value;
    }

    /**
     * 从request中取出Attribute值
     *
     * @param key     名称
     * @param request request
     * @param <T>     泛型
     * @return 取值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttr(String key, ServletRequest request) {
        Object value = request.getAttribute(key);
        return value == null ? null : (T) value;
    }
    // ----------------------- 设置 Attribute 属性------------------------------


    // ----------------------- 设置 Parameter 属性------------------------------
    /**
     * 从request中取出Parameter值
     *
     * @param key     名称
     * @param request request
     * @return 取值
     */
    public static String getString(String key, ServletRequest request) {
        return getString(key, "", request);
    }

    /**
     * 从request中取出Parameter值
     *
     * @param key          名称
     * @param defaultValue 默认值
     * @param request      request
     * @return 取值
     */
    public static String getString(String key, String defaultValue, ServletRequest request) {
        String value = request.getParameter(key);
        if (StringHelper.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 从request中取出Parameter值
     *
     * @param key     名称
     * @param request request
     * @return 取值
     */
    public static String[] getStringArray(String key, ServletRequest request) {
        return getStringArray(key, null, request);
    }

    /**
     * 从request中取出Parameter值
     *
     * @param key     名称
     * @param request request
     * @return 取值
     */
    public static String[] getStringArray(String key, String[] defaultValue, ServletRequest request) {
        String[] params = request.getParameterValues(key);
        if (ArrayHelper.isEmpty(params)) {
            return defaultValue;
        }
        return params;
    }

    /**
     * 从表单Parameter中提取数值，如果不存在，返回0
     *
     * @param fieldName 表单名称
     * @param request   request
     * @return 数值，如果不存在，返回0
     */
    public static int getInt(String fieldName, ServletRequest request) {
        return getInt(fieldName, 0, request);
    }

    /**
     * 从表单Parameter中提取数值，如果不存在，返回0
     *
     * @param fieldName    表单名称
     * @param defaultValue 找不到时返回默认值
     * @param request      request
     * @return 数值，如果不存在，返回0
     */
    public static int getInt(String fieldName, int defaultValue, ServletRequest request) {
        String value = getString(fieldName, request);
        if (StringHelper.isEmpty(value) || !StringHelper.isNumeric(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * 提取Parameter中的数字数组
     *
     * @param fieldName 字段名
     * @param request   request
     * @return 数字数组
     */
    public static int[] getIntArray(String fieldName, ServletRequest request) {
        return getIntArray(fieldName, null, request);
    }

    /**
     * 提取Parameter中的数字数组
     *
     * @param fieldName 字段名
     * @param request   request
     * @return 数字数组
     */
    public static int[] getIntArray(String fieldName, int[] defaultValue, ServletRequest request) {
        String[] array = getStringArray(fieldName, request);
        if (ArrayHelper.isEmpty(array)) {
            return defaultValue;
        }

        int len = array.length;
        int[] value = new int[len];
        for (int i = 0; i < len; i++) {
            if (StringHelper.isNumeric(array[i])) {
                value[i] = Integer.parseInt(array[i]);
            } else {
                logger.error(fieldName + "属性下标为[" + i + "]的值:" + array[i] + "不是int型");
            }
        }
        return value;
    }

    /**
     * 转发请求.
     *
     * @param request  HTTP请求.
     * @param response HTTP响应.
     * @param url      需转发到的URL.
     */
    public static void dispatchRequest(ServletRequest request, HttpServletResponse response, String url) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            logger.warn("转发失败:" + e.getMessage());
        }
    }

    /**
     * 获取当前的request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        return sra.getRequest();
    }



    // ----------------------- 调试用方法 ------------------------------

    public static void printAllParams(HttpServletRequest request) {
        System.out.println("参数长度:" + request.getParameterMap().entrySet().size());
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            System.out.println("key:" + entry.getKey() + ":" + Arrays.toString(entry.getValue()));
        }
    }

    public static void printAll(HttpServletRequest request) {
        System.out.println("请求路径:" + request.getRequestURL().toString() + ", " + request.getMethod());
        Enumeration<String> header = request.getHeaderNames();
        while (header.hasMoreElements()) {
            String key = header.nextElement();
            String value = request.getHeader(key);
            System.out.println("头部:[" + key + "]:[" + value + "]");
        }

        Enumeration<String> param = request.getParameterNames();
        while (header.hasMoreElements()) {
            String key = param.nextElement();
            String[] value1 = request.getParameterValues(key);
            System.out.println("参数1:[" + key + "]:" + Arrays.toString(value1));
            String value2 = request.getParameter(key);
            System.out.println("参数2:[" + key + "]:" + value2);
        }

        System.out.println("参数长度:" + request.getParameterMap().entrySet().size());
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            System.out.println("key:" + entry.getKey() + ":" + Arrays.toString(entry.getValue()));
        }
    }
}
