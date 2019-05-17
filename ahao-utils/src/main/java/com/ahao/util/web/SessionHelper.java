package com.ahao.util.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * Created by Ahaochan on 2017/8/21.
 * 页面session助手类
 */
public abstract class SessionHelper {
    private static final Logger logger = LoggerFactory.getLogger(SessionHelper.class);

    private SessionHelper(){

    }

    /**
     * 从ThreadLocal中获取HttpRequest
     */
    public static HttpSession getSession(){
        return RequestHelper.getRequest().getSession();
    }

    /**
     * 向SESSION中设置字符串
     *
     * @param key     名称
     * @param value   值
     * @param session session
     */
    public static <T> void set(String key, T value, HttpSession session) {
        if (StringUtils.isEmpty(key)) {
            logger.debug("key:" + key + "为空, 未向session存入" + value);
            return;
        }
        session.setAttribute(key, value);
    }

    /**
     * 从SESSION中取出值
     *
     * @param key     名称
     * @param session session
     * @param <T>     泛型
     * @return 取值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, HttpSession session) {
        Object value = session.getAttribute(key);
        return value == null ? null : (T) value;
    }

    /**
     * 从SESSION中取出值
     *
     * @param key          名称
     * @param defaultValue 取值失败的默认值
     * @param session      session
     * @param <T>          泛型
     * @return 取值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue, HttpSession session) {
        Object value = session.getAttribute(key);
        return value == null ? defaultValue : (T) value;
    }



    /**
     * 移除SESSION保存中的对象
     *
     * @param name    名称
     * @param session session
     */
    public static void remove(String name, HttpSession session) {
        // 判断SESSION是否存在该对象
        if (session.getAttribute(name) != null) {
            // 移除该对象
            session.removeAttribute(name);
        }
    }

}