package com.ahao.core.net.convert;

import com.ahao.core.util.lang.StringHelper;
import com.alibaba.fastjson.JSONArray;

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
        String json = StringHelper.toCharacters(origin, StandardCharsets.UTF_8);
        return JSONArray.parseArray(json);
    }
}
