package com.ahao.spring.cloud.zuul.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractFallbackProvider implements FallbackProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFallbackProvider.class);

    @Override
    public String getRoute() {
        // 注意: 这里是route的名称，不是服务的名称
        return routeName();
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new AbstractClientHttpResponse() {
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return AbstractFallbackProvider.this.getBody();
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }

    protected abstract String routeName();
    protected abstract InputStream getBody();
}
