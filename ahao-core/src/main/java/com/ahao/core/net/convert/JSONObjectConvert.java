package com.ahao.core.net.convert;

import com.ahao.core.util.JSONHelper;
import com.ahao.core.util.lang.StringHelper;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.Charset;

/**
 * Created by Ahaochan on 2017/8/14.
 * 转换为 json 格式数据
 */
public class JSONObjectConvert implements Convert<JSONObject> {
    @Override
    public Class<JSONObject> clazz() {
        return JSONObject.class;
    }

    @Override
    public JSONObject convert(byte[] origin) {
        String json = StringHelper.toCharacters(origin, Charset.forName("UTF-8"));
        return JSONHelper.toJSONObject(json);
    }
}
