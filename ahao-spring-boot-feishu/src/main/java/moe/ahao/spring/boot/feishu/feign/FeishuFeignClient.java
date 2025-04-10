package moe.ahao.spring.boot.feishu.feign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuCardMessageReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feishu", url = "https://open.feishu.cn", configuration = FeishuFeignClient.FeishuFeignClientConfig.class)
public interface FeishuFeignClient {
    @PostMapping(value = "/open-apis/bot/v2/hook/{token}", consumes = "application/json")
    CreateMessageResp sendMessage(@PathVariable String token, @RequestBody FeishuCardMessageReq req);


    class FeishuFeignClientConfig {
        @Bean
        public Gson gson() {
            return new GsonBuilder().create();
        }

        @Bean
        public Encoder feignEncoder(Gson gson) {
            return new GsonEncoder(gson);
        }

        @Bean
        public Decoder feignDecoder(Gson gson) {
            return new GsonDecoder(gson);
        }
    }
}
