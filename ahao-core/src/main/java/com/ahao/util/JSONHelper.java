package com.ahao.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Ahaochan on 2017/8/14.
 * <p>
 * JSON工具类
 */
public abstract class JSONHelper {
    private JSONHelper() {
    }

    public static JSONArray getArray() {
        return new JSONArray();
    }

    public static JSONObject getJSON() {
        return new JSONObject();
    }

    /**
     * 将 json字符串 解析为 JSON对象
     *
     * @param json json字符串
     * @return JSON对象
     */
    public static JSONObject getJSONObject(String json) {
        return JSONObject.parseObject(json);
    }

    /**
     * 将 对象 解析为 json字符串
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String getJSONString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * 将 json字符串 解析为 clazz对象
     *
     * @param json  json字符串
     * @param clazz 对象
     * @return clazz对象
     */
    public static <T> T getObject(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }
}
