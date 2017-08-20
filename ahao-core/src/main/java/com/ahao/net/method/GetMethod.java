package com.ahao.net.method;

import com.ahao.net.Parameterizable;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/14.
 * 进行 get 请求
 */
public class GetMethod extends BaseMethod<GetMethod> implements Parameterizable<GetMethod> {
    private Map<String, String> params = new LinkedHashMap<>();

    public GetMethod(String url) {
        super(url);
    }

    @Override
    public GetMethod addParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    @Override
    public GetMethod addParam(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }


    @Override
    public HttpRequestBase initHttpMethod(String url) {
        // 设置参数
        StringBuilder urlBuilder = new StringBuilder(url + "?");
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);


        HttpGet http = new HttpGet(urlBuilder.toString());
        // 设置头部
        for (Map.Entry<String, String> header : getHeader().entrySet()) {
            http.setHeader(header.getKey(), header.getValue());
        }
        return http;
    }


}