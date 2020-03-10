package com.ahao.spring.cloud.zuul.fallback;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SimpleFallback extends AbstractFallbackProvider {
    @Override
    protected String routeName() {
        return "*";
    }

    @Override
    protected InputStream getBody() {
        return new ByteArrayInputStream("系统繁忙".getBytes(StandardCharsets.UTF_8));
    }
}
