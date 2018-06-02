package com.ahao.core.net.param;

import org.apache.http.HttpEntity;

import java.util.Map;

public interface ParamFormatter {
    String contentType();

    HttpEntity format(Map<String, String> params);

    HttpEntity format(String requestBody);
}
