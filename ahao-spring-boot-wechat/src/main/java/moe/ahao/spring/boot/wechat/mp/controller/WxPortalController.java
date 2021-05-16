package moe.ahao.spring.boot.wechat.mp.controller;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;
import moe.ahao.spring.boot.wechat.mp.aop.AppIdCheckAOP;
import moe.ahao.spring.boot.wechat.mp.aop.SignCheckAOP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static moe.ahao.spring.boot.wechat.mp.aop.AppIdCheckAOP.APP_ID;

@RestController
@RequestMapping("/wx/portal/{" + APP_ID + "}")
public class WxPortalController {
    private static final Logger logger = LoggerFactory.getLogger(WxPortalController.class);

    private WxMpService wxMpService;
    private WxMpMessageRouter wxMpMessageRouter;

    public WxPortalController(WxMpService wxMpService, WxMpMessageRouter wxMpMessageRouter) {
        this.wxMpService = wxMpService;
        this.wxMpMessageRouter = wxMpMessageRouter;
    }

    /**
     * 接口配置, 完成服务器与微信的一次握手
     * 使用 AOP 进行参数校验
     * @param echoStr 随机字符串
     * @see AppIdCheckAOP
     * @see SignCheckAOP
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319">接入指南</a>
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(value = "echostr", required = false) String echoStr) {
        return echoStr;
    }

    /**
     * 微信推送数据总入口, 使用路由器分发消息
     * 使用 AOP 进行参数校验
     * @param requestBody  微信发送的 XML 请求体
     * @param signature    微信加密签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param openid       用户微信openid
     * @param encType
     * @param msgSignature
     * @see AppIdCheckAOP
     * @see SignCheckAOP
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319">接入指南</a>
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        boolean isAES = "aes".equalsIgnoreCase(encType);
        WxMpCryptUtil cryptUtil = null;
        if (isAES) {
            cryptUtil = new WxMpCryptUtil(wxMpService.getWxMpConfigStorage());
            requestBody = cryptUtil.decrypt(msgSignature, timestamp, nonce, requestBody);
        }

        String responseXml = doPost(requestBody);

        if (isAES) {
            responseXml = cryptUtil.encrypt(responseXml);
        }
        return responseXml;
    }

    private String doPost(String requestBody) {
        WxMpXmlMessage requestXml = WxMpXmlMessage.fromXml(requestBody);
        logger.debug("微信推送请求体为: {} ", requestXml.toString());
        WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(requestXml);
        if (outMessage == null) {
            return "";
        }
        String responseXml = outMessage.toXml();
        logger.debug("微信请求处理后的响应体为：{}", responseXml);
        return responseXml;
    }
}
