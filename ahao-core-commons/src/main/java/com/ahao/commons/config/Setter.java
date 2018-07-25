package com.ahao.commons.config;

import java.util.List;
import java.util.Map;

public class Setter {

    /**
     * 获取 int 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public static boolean getBoolean(String key) {
        return SystemConfig.instance().getBoolean(key);
    }

    /**
     * 获取 boolean 型数据
     * @param key  XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public static boolean getBoolean(String key, String attr) {
        return SystemConfig.instance().getBoolean(key, attr);
    }

    /**
     * 获取 boolean 型数据
     * @param key   XPath节点路径
     * @param attr  指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public static Map<String, Boolean> getBoolean(String key, String attr, String... attrs) {
        return SystemConfig.instance().getBoolean(key, attr, attrs);
    }

    /**
     * 获取 int 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public static int getInt(String key) {
        return SystemConfig.instance().getInt(key);
    }

    /**
     * 获取 int 型数据
     * @param key  XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public static int getInt(String key, String attr) {
        return SystemConfig.instance().getInt(key, attr);
    }

    /**
     * 获取 boolean 型数据
     * @param key   XPath节点路径
     * @param attr  指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public static Map<String, Integer> getInt(String key, String attr, String... attrs) {
        return SystemConfig.instance().getInt(key, attr, attrs);
    }

    /**
     * 获取 String 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public static String getString(String key) {
        return SystemConfig.instance().getString(key);
    }

    /**
     * 获取 String 型数据
     * @param key  XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public static String getString(String key, String attr) {
        return SystemConfig.instance().getString(key, attr);
    }

    /**
     * 获取 String 型数据
     * @param key   XPath节点路径
     * @param attr  指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public static Map<String, String> getString(String key, String attr, String... attrs) {
        return SystemConfig.instance().getString(key, attr, attrs);
    }

    /**
     * 获取 String 集合数据
     * @param key XPath节点路径
     */
    public static List<String> getStringList(String key) {
        return SystemConfig.instance().getStringList(key);
    }

}
