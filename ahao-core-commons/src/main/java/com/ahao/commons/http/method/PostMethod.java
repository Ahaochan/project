package com.ahao.commons.http.method;

import com.ahao.commons.http.param.Parameterizable;
import com.ahao.commons.util.lang.CollectionHelper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/14.
 * 使用 json 格式进行 post 请求
 */
public class PostMethod extends BaseMethod<PostMethod> implements Parameterizable<PostMethod> {
    private static final Logger logger = LoggerFactory.getLogger(PostMethod.class);

    private String requestBody;
    private Map<String, String> params = new LinkedHashMap<>();

    public PostMethod(String url) {
        super(url);
    }

    @Override
    public PostMethod addParam(String key, String value) {
        if(StringUtils.isNotEmpty(requestBody)){
            logger.warn("requestBody已有值:"+requestBody+", requestBody将会覆盖params");
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public PostMethod addParam(Map<String, String> params) {
        if(StringUtils.isNotEmpty(requestBody)){
            logger.warn("requestBody已有值:"+requestBody+", requestBody将会覆盖params");
        }
        this.params.putAll(params);
        return this;
    }

    @Override
    public PostMethod setRequestBody(String requestBody) {
        if(MapUtils.isNotEmpty(params)){
            logger.warn("params已有值:"+params+", requestBody将会覆盖params");
        }
        this.requestBody = requestBody;
        return this;
    }

    @Override
    protected HttpRequestBase initRequestBody(String url) {
        HttpPost http = new HttpPost(url);
        if(StringUtils.isNotEmpty(requestBody)){
            // 直接指定请求体
            HttpEntity entity = getFormatter().format(requestBody);
            http.setEntity(entity);
            logger.debug("["+url+"]: 初始化请求体:"+ requestBody);
        } else {
            // 键值对形式设置请求体
            HttpEntity entity = getFormatter().format(params);
            http.setEntity(entity);
            logger.debug("["+url+"]: 初始化请求体:"+ params);
        }
        return http;
    }
}