package moe.ahao.spring.boot.okhttp3.config;

import moe.ahao.spring.boot.okhttp3.interceptor.LogInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttp3Config {

    @Bean
    public OkHttpClient okHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
//            .sslSocketFactory(sslSocketFactory(), x509TrustManager())
            .connectionPool(pool())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);

        interceptors.forEach(builder::addInterceptor);
        return builder.build();
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }
}
