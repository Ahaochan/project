package moe.ahao.spring.boot.wechat.mp.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LocationHandler implements WxMpMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(LocationHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        Double latitude = wxMessage.getLatitude();   // 纬度
        Double longitude = wxMessage.getLongitude(); // 经度
        Double precision = wxMessage.getPrecision(); // 精度
        logger.info("{} 上报地理位置，纬度 : {}，经度 : {}，精度 : {}", wxMessage.getFromUser(), latitude, longitude, precision);

        // 必须 return null, 避免对用户造成骚扰
        return null;
    }
}
