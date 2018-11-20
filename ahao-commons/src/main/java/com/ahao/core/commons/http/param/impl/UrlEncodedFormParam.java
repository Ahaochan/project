package com.ahao.core.commons.http.param.impl;

import com.ahao.core.commons.http.param.ParamFormatter;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlEncodedFormParam implements ParamFormatter {
    @Override
    public String contentType() {
        return null;
    }

    @Override
    public HttpEntity format(Map<String, String> params) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
        return postEntity;
    }

    @Override
    public HttpEntity format(String requestBody) {
        return new StringEntity(requestBody, "UTF-8");
    }
}