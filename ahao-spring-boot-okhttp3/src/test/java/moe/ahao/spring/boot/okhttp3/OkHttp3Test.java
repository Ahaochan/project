package moe.ahao.spring.boot.okhttp3;

import com.ahao.util.commons.io.JSONHelper;
import moe.ahao.spring.boot.Starter;
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
@ContextConfiguration(classes = Starter.class)
class OkHttp3Test {

    @Autowired
    private OkHttpClient client;

    @Test
    void get() {
        String url = "https://api.github.com/users/Ahaochan";
        Request request = new Request.Builder()
            .url(url)
            .build();

        Call call = client.newCall(request);
        try (Response response = call.execute();
             ResponseBody body = response.body()) {

            Assertions.assertNotNull(body);

            String login = JSONHelper.getString(body.string(), "login");

            Assertions.assertEquals("Ahaochan", login);
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }
    }
}
