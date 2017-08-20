package com.ahao.net.method;

import com.ahao.net.HttpClientHelper;
import com.ahao.net.Response;
import com.ahao.util.IOHelper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public BaseMethod(String url) {
        this.url = url;
    }

    /**
     * 加入请求头, 注意并未实际加入, 实际加入要在 {@link #initHttpMethod} 方法实现
     *
     * @param key   键
     * @param value 值
     * @return 返回this自身, 用于链式调用
     */
    public M addHeader(String key, String value) {
        header.put(key, value);
        return (M) this;
    }

    /**
     * 加入请求头, 注意并未实际加入, 实际加入要在 {@link #initHttpMethod} 方法实现
     *
     * @param headers 请求头集合
     * @return 返回this自身, 用于链式调用
     */
    public M addHeader(Map<String, String> headers) {
        header.putAll(headers);
        return (M) this;
    }


    /**
     * 模板方法, 交由子类实现
     * 初始化请求方法, 配置请求头和请求体
     *
     * @param url 请求路径
     * @return 返回 apache 的请求实体, 如 {@link org.apache.http.client.methods.HttpPost} 等.
     */
    protected abstract HttpRequestBase initHttpMethod(String url);

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
        // 打开 HttpClientUtils 客户端
        try (CloseableHttpClient httpClient = HttpClientHelper.getClient(https)) {
            // 初始化请求头和请求体
            HttpRequestBase http = initHttpMethod(url);

            // 设置超时限制
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(1000 * 100)
                    .build();
            http.setConfig(requestConfig);

            // 获取 response
            try (CloseableHttpResponse response = httpClient.execute(http);
                 InputStream inputStream = response.getEntity().getContent()) {

                byte[] data = IOHelper.toByte(inputStream);

                // 将返回的 byte[] 封装到 Response 中, 用于格式化.
                return new Response(data);
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("IO异常: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("IO异常: " + e.getMessage());
        }
        return null;
    }

    // ---------------------返回头部信息-------------------------------
    public Map<String, String> getHeader() {
        return header;
    }
}
