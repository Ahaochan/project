package com.ahao.net.method;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/10.
 * 使用 text 格式进行 post 请求
 */
public class PostTextMethod extends BaseMethod<PostTextMethod> {
    private static final Logger logger = LoggerFactory.getLogger(PostTextMethod.class);

    private String params = "";

    public PostTextMethod(String url) {
        super(url);
        // 必须加入的请求头, 表示文本传输
        super.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
    }

    public PostTextMethod setParams(String params) {
        this.params = params;
        return this;
    }

    @Override
    protected HttpRequestBase initHttpMethod(String url) {
        HttpPost http = new HttpPost(url);
        for (Map.Entry<String, String> header : getHeader().entrySet()) {
            http.setHeader(header.getKey(), header.getValue());
        }
        logger.debug("["+url+"]: 初始化请求头:"+ JSONObject.toJSONString(getHeader()));

        // 使用json的请求体
        http.setEntity(new StringEntity(params, "UTF-8"));
        logger.debug("["+url+"]: 初始化请求体:"+ params);
        return http;
    }
}
