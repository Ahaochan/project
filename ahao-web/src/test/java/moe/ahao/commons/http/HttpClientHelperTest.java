package moe.ahao.commons.http;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpClientHelperTest {

    @Test
    public void connectBaidu() {
        int code = HttpClientHelper.get("http://www.baidu.com")
                .execute()
                .getStatusCode();
        Assertions.assertEquals(200, code);
    }
}
