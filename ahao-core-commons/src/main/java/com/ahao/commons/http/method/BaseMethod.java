package com.ahao.commons.http.method;

import com.ahao.commons.http.HttpClientHelper;
import com.ahao.commons.http.base.Response;
import com.ahao.commons.http.param.ParamFormatter;
import com.ahao.commons.http.param.impl.UrlEncodedFormParam;
import com.ahao.commons.util.io.IOHelper;
import com.ahao.commons.util.lang.ReflectHelper;
import com.ahao.commons.util.lang.time.DateHelper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahaochan on 2017/8/10.
 * 传输方法基类, 泛型 M 用于链式调用, 返回自身
 * 1. url 请求地址
 * 2. header 请求头
 */
@SuppressWarnings("unchecked")
public abstract class BaseMethod<M extends BaseMethod> {
    private static final Logger logger = LoggerFactory.getLogger(BaseMethod.class);

    private String url;
    private Map<String, String> header = new LinkedHashMap<>();
    private ParamFormatter paramFormatter = new UrlEncodedFormParam();

    public BaseMethod(String url) {
        this.url = url;
    }

    /**
     * 加入请求头, 注意并未实际加入, 在{@link #execute()}中实现
     *
     * @param key   键
     * @param value 值
     * @return 返回this自身, 用于链式调用
     */
    @SuppressWarnings("unchecked")
    public M addHeader(String key, String value) {
        header.put(key, value);
        return (M) this;
    }

    /**
     * 加入请求头, 注意并未实际加入, 在{@link #execute()}中实现
     *
     * @param headers 请求头集合
     * @return 返回this自身, 用于链式调用
     */
    @SuppressWarnings("unchecked")
    public M addHeader(Map<String, String> headers) {
        header.putAll(headers);
        return (M) this;
    }

    /**
     * @param paramFormatter 不同数据源的参数格式化器
     * @return 返回this自身, 用于链式调用
     */
    @SuppressWarnings("unchecked")
    public M paramType(ParamFormatter paramFormatter) {
        this.paramFormatter = paramFormatter;
        return (M) this;
    }

    /**
     * @param paramFormatter 不同数据源的参数格式化器
     * @return 返回this自身, 用于链式调用
     */
    @SuppressWarnings("unchecked")
    public <T  extends ParamFormatter> M paramType(Class<T> paramFormatter) {
        this.paramFormatter = ReflectHelper.create(paramFormatter);
        return (M) this;
    }

    /**
     * 模板方法, 交由子类实现
     * 初始化请求方法, 配置请求体
     *
     * @param url 请求路径
     * @return 返回 apache 的请求实体, 如 {@link org.apache.http.client.methods.HttpPost} 等.
     */
    protected abstract HttpRequestBase initRequestBody(String url);

    /**
     * 默认不使用 https
     * 执行请求, 将返回的 byte[] 封装到 Response 中, 用于格式化.
     *
     * @return 响应体, 用于格式化数据
     */
    public Response execute() {
        return execute(false);
    }

    /**
     * 执行请求, 将返回的 byte[] 封装到 Response 中, 用于格式化.
     *
     * @param https true为使用https
     * @return 响应体, 用于格式化数据
     */
    public Response execute(boolean https) {
        // 1. 初始化请求头和请求体
        HttpRequestBase http = initRequestBody(url);
        // 1.1 初始化请求头
        String contentType = paramFormatter.contentType();
        if(StringUtils.isNotEmpty(contentType)){
            header.put(HttpHeaders.CONTENT_TYPE, contentType);
        }
        for (Map.Entry<String, String> entry : header.entrySet()) {
            http.addHeader(entry.getKey(), entry.getValue());
        }
        logger.debug("["+url+"]: 初始化请求头:"+ JSONObject.toJSONString(header));

        // 2. 配置http, 设置超时限制
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000 * 100)
                .build();
        http.setConfig(requestConfig);

        long start = System.currentTimeMillis();
        logger.debug("[" + url + ": 开始进行Http请求, 开始时间: " + DateHelper.getString(start, DateHelper.yyyyMMdd_hhmmssSSS));

        // 3. 打开 HttpClientUtils 客户端
        try (CloseableHttpClient httpClient = HttpClientHelper.getClient(https);
             CloseableHttpResponse response = httpClient.execute(http)) {
            // 获取IO
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            // 获取 字节数组
            byte[] data = IOHelper.toByte(inputStream);

            // 释放IO
            IOUtils.closeQuietly(inputStream);
            EntityUtils.consume(entity);

            // 将返回的 byte[] 封装到 Response 中, 用于格式化.
            return new Response(url, response.getStatusLine().getStatusCode(), data);
        } catch (IOException e) {
            logger.warn("[" + url + "]@: IO异常: ", e);
        } finally {
            long end = System.currentTimeMillis();
            logger.debug("[" + url + ": Http请求完毕: 结束时间: " + DateHelper.getString(end, DateHelper.yyyyMMdd_hhmmssSSS) +
                    ", 总耗时: " + DateHelper.getBetween(start, end, TimeUnit.MILLISECONDS) + "毫秒");
            http.releaseConnection();
        }
        return new Response(url, -1, new byte[0]);
    }


    protected ParamFormatter getFormatter(){
        if(paramFormatter == null){
            return new UrlEncodedFormParam();
        }
        return paramFormatter;
    }

    // ---------------------返回头部信息-------------------------------
    public Map<String, String> getHeader() {
        return header;
    }
}
