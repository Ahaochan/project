package com.ahao.net;

import com.ahao.net.method.GetMethod;
import com.ahao.net.method.PostJSONMethod;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/7/6.
 * <p>
 * 简单的 apache HttpClient 封装,
 * 1. http请求的入口
 * 2. 创建HttpClient, 支持https
 * <p>
 * 简单使用方法:
 * HttpClientHelper.postJson("http://baidu.com")
 * .addHeader(key1, value1) // 添加头部
 * .addParam(key2, value2) // 添加参数
 * .execute(isHttps) // 执行, 是否https传输
 * .adapter(Convert<JSONObject>.class, Adapter<JSONObject>.class);
 * // Convert转换器将byte数组转为想要的数据
 * // Adapter适配器对转换后的数据进行二次加工
 */
public abstract class HttpClientHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    /**
     * 使用 json 格式进行 post 传输
     *
     * @param url 访问 url 地址
     * @return 返回 PostJSONMethod 用于链式调用
     */
    public static PostJSONMethod postJson(String url) {
        return new PostJSONMethod(url);
    }

    public static GetMethod get(String url) {
        return new GetMethod(url);
    }

    /**
     * SSL, 支持Https
     */
    private static SSLConnectionSocketFactory factory = SSLConnectionSocketFactory.getSocketFactory();


    /**
     * 获取 HttpsClient 用于 Http 请求
     *
     * @param isHttps 是否https协议
     * @return HttpsClient 用于 Http 请求
     */
    public static CloseableHttpClient getClient(boolean isHttps) {
        return isHttps ? getHttpsClient() : getHttpClient();
    }

    /**
     * 获取 HttpsClient 用于 Https 请求
     *
     * @return HttpsClient 用于 Https 请求
     */
    public static CloseableHttpClient getHttpsClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(factory)
                .build();
    }

    /**
     * 获取 HttpsClient 用于 Http 请求
     *
     * @return HttpsClient 用于 Http 请求
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }
}