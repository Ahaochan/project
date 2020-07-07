package moe.ahao.spring.boot.okhttp3;

import moe.ahao.spring.boot.okhttp3.config.OkHttp3Config;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {OkHttp3Config.class})
public class OkHttp3Test {

    @Autowired
    private OkHttpClient client;

    @Test
    public void get() {
        String url = "https://bing.com";
        Request request = new Request.Builder()
            .url(url)
            .build();

        Call call = client.newCall(request);
        try (Response response = call.execute();
             ResponseBody body = response.body()) {

            Assertions.assertNotNull(body);
            System.out.println(body.string());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }
    }
}
