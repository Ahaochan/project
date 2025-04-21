package moe.ahao.spring.boot.feishu.feign;

import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuCardTemplateSendReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feishu", url = "https://open.feishu.cn")
public interface FeishuFeignClient {
    @PostMapping(value = "/open-apis/bot/v2/hook/{token}", consumes = "application/json")
    <T> String sendMessage(@PathVariable String token, @RequestBody FeishuCardTemplateSendReq<T> req);
}
