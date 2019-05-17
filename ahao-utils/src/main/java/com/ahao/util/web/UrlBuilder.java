package com.ahao.util.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Avalon on 2017/5/22.
 *
 * 生成url
 */
public class UrlBuilder {
    private static final Logger logger = LoggerFactory.getLogger(UrlBuilder.class);


    /** 根url */
    private String baseUrl;
    /** 拼接了请求参数的url */
    private StringBuilder url;
    /** restful参数, 格式为 /key1/value1/key2/value2 */
    private Map<String, Object> restfulParams;
    /** request参数, 格式为 key1=value1&key2=value2 */
    private Map<String, Object> requestParams;

    public UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.url = new StringBuilder(baseUrl);
        this.restfulParams = new LinkedHashMap<>();
        this.requestParams = new LinkedHashMap<>();
    }

    public UrlBuilder(UrlBuilder urlBuilder) {
        this.baseUrl = urlBuilder.baseUrl;
        this.url = new StringBuilder(urlBuilder.baseUrl);
        this.restfulParams = new LinkedHashMap<>(urlBuilder.restfulParams);
        this.requestParams = new LinkedHashMap<>(urlBuilder.requestParams);
    }

    public String build() {
        UrlBuilder builder = new UrlBuilder(this);
        // 拼接 restful 参数
        String restfulParamsUrl = builder.restfulParams.entrySet().stream()
                .map(e -> "/" + e.getKey() + "/" + e.getValue())
                .collect(Collectors.joining());
        // 拼接 request 参数
        String requestParamsUrl = builder.requestParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        // 避免出现http://www.abc.com/的情况
        if (builder.url.charAt(builder.url.length() - 1) == '/') {
            builder.url.deleteCharAt(builder.url.length() - 1);
        }
        return builder.url.toString() + restfulParamsUrl + "?" + requestParamsUrl;
    }


    public UrlBuilder restUrl(String key, Object value) {
        if (StringUtils.isNotEmpty(key) && value != null) {
            restfulParams.put(key, value);
        } else {
            logger.warn("key:" + key + " or value:" + value + " must not Null!!!");
        }
        return this;
    }

    public UrlBuilder restUrl(Map<String, Object> param) {
        param.forEach(this::restUrl);
        return this;
    }

    public UrlBuilder param(String key, Object value) {
        if (StringUtils.isNotEmpty(key) && value != null) {
            this.requestParams.put(key, value);
        } else {
            logger.warn("key:" + key + " or value:" + value + " must not Null!!!");
        }
        return this;
    }

    public UrlBuilder param(Map<String, Object> param) {
        param.forEach(this::param);
        return this;
    }

    @Override
    public String toString() {
        return build();
    }
}
