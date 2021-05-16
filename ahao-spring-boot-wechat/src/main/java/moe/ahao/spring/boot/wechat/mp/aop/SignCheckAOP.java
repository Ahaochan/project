package moe.ahao.spring.boot.wechat.mp.aop;

import me.chanjar.weixin.mp.api.WxMpService;
import moe.ahao.util.web.RequestHelper;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SignCheckAOP {
    public static final int TIMEOUT = 60 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(SignCheckAOP.class);

    private WxMpService wxMpService;
    public SignCheckAOP(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Before("execution(* moe.ahao.spring.boot.wechat.mp.controller.WxPortalController.*(..))")
    public void checkSign() {
        HttpServletRequest request = RequestHelper.getRequest();

        // 1. 判断是否有 signature、timestamp、nonce 参数
        String signature = RequestHelper.getString("signature", request);
        String timestamp = RequestHelper.getString("timestamp", request);
        String nonce     = RequestHelper.getString("nonce", request);
        logger.debug("微信签名校验参数, signature:{}, timestamp:{}, nonce:{}", signature, timestamp, nonce);

        // 2. 进行签名校验
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求, 可能属于伪造的请求!");
        }

        // 3. 安全性检测
        if(Long.parseLong(timestamp) - System.currentTimeMillis() > TIMEOUT) {
            throw new IllegalArgumentException("请求超时, 时间戳与现在时间间隔过长!");
        }
        // TODO signature 防重用
    }
}
