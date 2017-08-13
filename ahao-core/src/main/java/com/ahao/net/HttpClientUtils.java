package com.ahao.net;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ahaochan on 2017/7/6.
 * 对Apache HttpClient简单封装, 返回JSON数据
 */
public class HttpClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 单例模式
     */
    private volatile static HttpClientUtils instance;

    /**
     * 支持Https
     */
    private static SSLConnectionSocketFactory factory = SSLConnectionSocketFactory.getSocketFactory();

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(factory)
                .build();
    }

    public static HttpClientUtils getInstance() {
        if (instance == null) {
            synchronized (HttpClientUtils.class) {
                if (instance == null) {
                    instance = new HttpClientUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 无参数的post请求
     *
     * @param url 请求url
     * @return 返回json
     */
    public JSONObject post(@NotNull String url) {
        return post(url, new JSONObject());
    }

    /**
     * 使用json作为参数的post请求
     *
     * @param url    请求url
     * @param params 参数
     * @return 返回响应体response
     */
    public JSONObject post(@NotNull String url, @NotNull JSONObject params) {
        return JSONObject.parseObject(post(url, params.toJSONString()));
    }

    /**
     * post请求
     *
     * @param url    请求url
     * @param params 参数
     * @return 返回响应体response
     */
    private String post(@NotNull String url, @NotNull String params) {
        // 打开 HttpClientUtils 客户端
        try (CloseableHttpClient httpClient = getHttpClient()) {
            // 传入 url
            HttpPost httpPost = new HttpPost(url);
            // 传入 header 信息
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            // 传入 参数 信息
            httpPost.setEntity(new StringEntity(params, "UTF-8"));

            // 设置超时限制
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(1000 * 100)
                    .build();
            httpPost.setConfig(requestConfig);

            // 获取 response
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity resEntity = response.getEntity();
                return EntityUtils.toString(resEntity, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("IO异常: " + e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.warn("url参数编码错误: " + e.getMessage());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.warn("客户端协议错误: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("IO异常: " + e.getMessage());
        }
        return null;
    }
}