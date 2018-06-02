package com.ahao.core.net.param.impl;

import com.ahao.core.net.param.ParamFormatter;
import com.ahao.core.util.JSONHelper;
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
        String jsonParam = JSONHelper.toJSONString(params);
        return new StringEntity(jsonParam, "UTF-8");
    }

    @Override
    public HttpEntity format(String requestBody) {
        return new StringEntity(requestBody, "UTF-8");
    }
}
