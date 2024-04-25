package moe.ahao.spring.boot.wechat.mp.feign;

import moe.ahao.spring.boot.wechat.mp.feign.dto.WechatGetUnlimitedQRCodeQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://api.weixin.qq.com", name = "WechatQRCodeFeignClient", path = "/wxa")
public interface WechatQRCodeFeignClient {
    @PostMapping("/getwxacodeunlimit")
    byte[] getUnlimitedQRCode(@RequestParam("access_token") String accessToken, @RequestBody WechatGetUnlimitedQRCodeQuery query);
}
