package com.ahao.commons.http;

import org.junit.Assert;
import org.junit.Test;

public class HttpClientHelperTest {

    @Test
    public void connectBaidu() {
        int code = HttpClientHelper.get("http://www.baidu.com")
                .execute()
                .getStatusCode();
        Assert.assertEquals(200, code);
    }
}