package moe.ahao.commons.http.param.impl;

import moe.ahao.commons.http.param.ParamFormatter;
import moe.ahao.util.commons.io.JSONHelper;
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
        return new StringEntity(JSONHelper.toString(params), "UTF-8");
    }

    @Override
    public HttpEntity format(String params) {
        // 使用json的请求体
        return new StringEntity(params, "UTF-8");
    }
}
