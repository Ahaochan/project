package com.ahao.core.net.convert;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

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
        String json = StringUtils.toEncodedString(origin, StandardCharsets.UTF_8);
        if(StringUtils.isEmpty(json)){
            return new JSONObject();
        }
        return JSONObject.parseObject(json);
    }
}
