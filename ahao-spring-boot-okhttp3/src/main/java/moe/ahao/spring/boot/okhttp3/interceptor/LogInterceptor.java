package moe.ahao.spring.boot.okhttp3.interceptor;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Headers headers = request.headers();
        logger.debug("请求头数量:{}", headers.size());
        headers.toMultimap().forEach((key, value) -> logger.debug("{}: {}", key, value));

        HttpUrl url = request.url();        // http://127.0.0.1/test/upload/img?username=Ahaochan&password=12345
        logger.debug("请求:"+url);

        return chain.proceed(request);
    }
}
