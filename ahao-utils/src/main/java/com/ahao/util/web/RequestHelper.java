package com.ahao.util.web;

import com.ahao.util.commons.lang.math.NumberHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class RequestHelper {
    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    private RequestHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 从ThreadLocal中获取HttpRequest
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
    }

    /**
     * 从ThreadLocal中获取项目路径
     */
    public static String getContextPath(){
        return getRequest().getContextPath();
    }

    // ----------------------- 获取 Header 属性------------------------------
    public static String getHeader(String key) {
        HttpServletRequest request = getRequest();
        return getHeader(key, null, request);
    }

    public static String getHeader(String key, String defaultValue) {
        HttpServletRequest request = getRequest();
        return getHeader(key, defaultValue, request);
    }

    public static String getHeader(String key,  String defaultValue, HttpServletRequest request) {
        if(StringUtils.isEmpty(key) || request == null) {
            return "";
        }
        String value = request.getHeader(key);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public static List<String> getHeaders(String key) {
        HttpServletRequest request = getRequest();
        return getHeaders(key, request);
    }

    public static List<String> getHeaders(String key, HttpServletRequest request) {
        if(StringUtils.isEmpty(key) || request == null) {
            return Collections.emptyList();
        }
        return Collections.list(request.getHeaders(key));
    }
    // ----------------------- 获取 Header 属性------------------------------

    // ----------------------- 设置 Attribute 属性------------------------------
    /**
     * 向request中设置Attribute字符串
     *
     * @param key     名称
     * @param value   值
     * @param request request
     */
    public static <T> void setAttr(String key, T value, ServletRequest request) {
        if (StringUtils.isEmpty(key)) {
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
        if (StringUtils.isEmpty(value)) {
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
        if (ArrayUtils.isEmpty(params)) {
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
        if (StringUtils.isEmpty(value) || !NumberHelper.isNumber(value)) {
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
        if (ArrayUtils.isEmpty(array)) {
            return defaultValue;
        }

        int len = array.length;
        int[] value = new int[len];
        for (int i = 0; i < len; i++) {
            if (NumberHelper.isNumber(array[i])) {
                value[i] = Integer.parseInt(array[i]);
            } else {
                logger.error(fieldName + "属性下标为[" + i + "]的值:" + array[i] + "不是int型");
            }
        }
        return value;
    }

    /**
     * 判断user-agent是否为桌面端
     */
    public static boolean isPC(){
        String[] mobileUserAgent = {"ANDROID", "IPHONE", "SYMBIANOS", "WINDOWS PHONE", "IPAD", "IPOD"};
        String userAgent = getHeader("User-Agent", "").toUpperCase();
        boolean isMobile = StringUtils.containsAny(userAgent, mobileUserAgent);
        return !isMobile;
    }

    /**
     * 判断user-agent是否为IE浏览器
     */
    public static boolean isIE() {
        String[] ieUserAgent = {"MSIE", "TRIDENT", "EDGE"};
        String userAgent = getHeader("User-Agent", "").toUpperCase();
        return StringUtils.containsAny(userAgent, ieUserAgent);
    }

    public static String safetyFilename(String filename) {
        try {
            if (isIE()) {
                filename = URLEncoder.encode(filename, "UTF-8");
                filename = StringUtils.replace(filename, "+", "%20");
            } else {
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("文件名{}转换编码失败, {}", filename, e.getMessage());
        }
        return filename;
    }

    /**
     * 转发请求.
     * @param request  HTTP请求.
     * @param response HTTP响应.
     * @param url      需转发到的URL.
     */
    public static void dispatchRequest(ServletRequest request, HttpServletResponse response, String url) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException e) {
            logger.warn("转发失败:", e);
        }
    }

    /**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，
     * 但是经过nginx等反向代理软件后，这个方法会失效。
     * 本方法先从Header中获取X-Real-IP，
     * 如果不存在再从X-Forwarded-For获得第一个IP(用,分割)
     * 如果还不存在则调用Request .getRemoteAddr()。
     */
    public static String getClientIp() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = request.getHeader("X-Real-IP");
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            } else {
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("获取Ip地址失败:", e);
        }
        return "非法Ip";
    }
}
