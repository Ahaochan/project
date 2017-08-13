package com.ahao.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class RequestHelper {

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
