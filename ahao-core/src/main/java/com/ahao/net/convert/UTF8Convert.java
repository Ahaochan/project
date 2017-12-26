package com.ahao.net.convert;

import com.ahao.util.lang.StringHelper;

import java.nio.charset.Charset;

/**
 * Created by Ahaochan on 2017/8/14.
 * 转换为 string 格式数据
 */
public class UTF8Convert implements Convert<String> {

    @Override
    public Class<String> clazz() {
        return String.class;
    }

    @Override
    public String convert(byte[] origin) {
        return StringHelper.toCharacters(origin, Charset.forName("UTF-8"));
    }
}
