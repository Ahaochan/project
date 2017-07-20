package com.ahao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Ahaochan on 2017/7/16.
 */
public abstract class JSONHelper {

    public static String toString(JSONObject json){
        return json.toJSONString();
    }

    public static String toString(Object obj){
        return JSONObject.toJSONString(obj);
    }

    public static JSONObject toJSON(String json){
        return JSON.parseObject(json);
    }
}
