package com.ahao.commons.http.convert;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * Created by Ahaochan on 2017/8/10.
 * <p>
 * 转换为 json 格式数据
 */
public class JSONArrayConvert implements Convert<JSONArray> {
    @Override
    public Class<JSONArray> clazz() {
        return JSONArray.class;
    }

    @Override
    public JSONArray convert(byte[] origin) {
        String json = StringUtils.toEncodedString(origin, StandardCharsets.UTF_8);
        return JSONArray.parseArray(json);
    }
}
