package com.ahao.core.commons.http.param;

import org.apache.http.HttpEntity;

import java.util.Map;

public interface ParamFormatter {
    String contentType();

    HttpEntity format(Map<String, String> params);

    HttpEntity format(String requestBody);
}
