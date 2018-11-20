package com.ahao.core.commons.http.param.impl;

import com.ahao.core.commons.http.param.ParamFormatter;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.util.Map;

public class TextPlainParam implements ParamFormatter {
    @Override
    public String contentType() {
        return "text/plain";
    }

    @Override
    public HttpEntity format(Map<String, String> params) {
        return new StringEntity(JSONObject.toJSONString(params), "UTF-8");
    }

    @Override
    public HttpEntity format(String params) {
        // 使用json的请求体
        return new StringEntity(params, "UTF-8");
    }
}
