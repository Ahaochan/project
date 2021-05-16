package moe.ahao.commons.http.method;

import moe.ahao.commons.http.param.Parameterizable;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/14.
 * 进行 get 请求
 */
public class GetMethod extends BaseMethod<GetMethod> implements Parameterizable<GetMethod> {
    private static final Logger logger = LoggerFactory.getLogger(GetMethod.class);

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
    public GetMethod setRequestBody(String requestBody) {
        logger.error("get方法不允许指定请求体requestBody, 请在params参数中指定请求参数");
        return this;
    }

    @Override
    public HttpRequestBase initRequestBody(String url) {
        // 设置参数
        StringBuilder urlBuilder = new StringBuilder(url + "?");
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        HttpGet http = new HttpGet(urlBuilder.toString());
        logger.debug("["+url+"]: 初始化请求体:"+ urlBuilder.toString());
        return http;
    }
}
