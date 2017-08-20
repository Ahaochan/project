package com.ahao.net.method;

import com.ahao.net.Parameterizable;
import com.ahao.util.JSONHelper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/14.
 * 使用 json 格式进行 post 请求
 */
public class PostJSONMethod extends BaseMethod<PostJSONMethod> implements Parameterizable<PostJSONMethod> {
    private static final Logger logger = LoggerFactory.getLogger(PostJSONMethod.class);

    private Map<String, String> params = new LinkedHashMap<>();

    public PostJSONMethod(String url) {
        super(url);
        // 必须加入的请求头, 表示json传输
        super.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @Override
    public PostJSONMethod addParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    @Override
    public PostJSONMethod addParam(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }


    @Override
    protected HttpRequestBase initHttpMethod(String url) {
        HttpPost http = new HttpPost(url);
        for (Map.Entry<String, String> header : getHeader().entrySet()) {
            http.setHeader(header.getKey(), header.getValue());
        }

        // 使用json的请求体
        String jsonParam = JSONHelper.getJSONString(params);
        http.setEntity(new StringEntity(jsonParam, "UTF-8"));
        return http;
    }
}