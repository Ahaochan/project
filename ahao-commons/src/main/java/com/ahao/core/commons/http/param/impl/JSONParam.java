package com.ahao.core.commons.http.param.impl;

import com.ahao.core.commons.http.param.ParamFormatter;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.util.Map;

public class JSONParam implements ParamFormatter {
    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public HttpEntity format(Map<String, String> params) {
        String jsonParam = JSONObject.toJSONString(params);
        return new StringEntity(jsonParam, "UTF-8");
    }

    @Override
    public HttpEntity format(String requestBody) {
        return new StringEntity(requestBody, "UTF-8");
    }
}
