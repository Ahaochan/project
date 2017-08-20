package com.ahao.net;

import com.ahao.net.method.BaseMethod;

import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/10.
 * <p>
 * 考虑到文件上传method没有参数的情况, 将请求体参数从BaseMethod抽取出来
 */
public interface Parameterizable<T extends BaseMethod> {
    T addParam(String key, String value);

    T addParam(Map<String, String> params);
}