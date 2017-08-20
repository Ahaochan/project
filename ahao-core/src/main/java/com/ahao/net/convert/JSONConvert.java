package com.ahao.net.convert;

import com.ahao.util.JSONHelper;
import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Ahaochan on 2017/8/14.
 * 转换为 json 格式数据
 */
public class JSONConvert implements Convert<JSONObject> {
    @Override
    public JSONObject convert(byte[] origin) {
        String json = StringHelper.toUTF8(origin);
        return JSONHelper.getJSONObject(json);
    }
}
