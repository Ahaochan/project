package moe.ahao.spring.cloud.alibaba.oss.service;

import moe.ahao.util.commons.lang.time.DateHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface AlibabaOssService {
    String putObject(String key, File value);
    String putObject(String key, InputStream value);
    default String putObject(String key, byte[] value) {
        return putObject(key, new ByteArrayInputStream(value));
    }
    default String putObject(String key, URL value) throws IOException {
        return putObject(key, value.openStream());
    }

    String getUrl(String key, Date expireTime);
    default String getUrl(String key, long expire, TimeUnit timeUnit) {
        Date expireTime = DateHelper.addTime(new Date(), expire, timeUnit);
        return getUrl(key, expireTime);
    }
}
