package com.ahao.net.convert;

import com.ahao.util.StringHelper;

/**
 * Created by Ahaochan on 2017/8/14.
 * 转换为 string 格式数据
 */
public class StringConvert implements Convert<String> {

    @Override
    public String convert(byte[] origin) {
        return StringHelper.toUTF8(origin);
    }
}
