package moe.ahao.spring.boot.wechat.mp.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.spring.boot.wechat.mp.feign.WechatQRCodeFeignClient;
import moe.ahao.spring.boot.wechat.mp.feign.dto.WeChatBaseResp;
import moe.ahao.spring.boot.wechat.mp.feign.dto.WechatGetUnlimitedQRCodeQuery;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WeChatQRCodeComponent {
    @Autowired
    private WechatQRCodeFeignClient wechatQRCodeFeignClient;

    public byte[] getUnlimitedQRCode(String accessToken, WechatGetUnlimitedQRCodeQuery query) {
        byte[] unlimitedQRCode = wechatQRCodeFeignClient.getUnlimitedQRCode(accessToken, query);

        String json = new String(unlimitedQRCode, StandardCharsets.UTF_8);
        WeChatBaseResp resp = JSONHelper.parse(json, WeChatBaseResp.class);
        if(resp != null) {
            log.warn("获取不限制的小程序码错误, errcode:{}, errmsg:{}", resp.getErrCode(), resp.getErrMsg());
            return new byte[0];
        } else {
            byte[] binaryData = unlimitedQRCode;
            return binaryData;
        }
    }
}
