package moe.ahao.spring.boot.wechat.mp.aop;

import me.chanjar.weixin.mp.api.WxMpService;
import moe.ahao.util.web.RequestHelper;
import org.apache.commons.collections4.MapUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
public class AppIdCheckAOP {
    public static final String APP_ID = "wx_mp_appid";

    private WxMpService wxMpService;
    public AppIdCheckAOP(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Before("execution(* moe.ahao.spring.boot.wechat.mp.controller.WxPortalController.*(..))")
    public void checkAppId() {
        HttpServletRequest request = RequestHelper.getRequest();

        // 1. 判断是否有 path 参数
        Map<String, String> pathVariables = RequestHelper.getAttr(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, request);
        if(MapUtils.isEmpty(pathVariables)) {
            throw new IllegalArgumentException("Request 中 appid 参数不存在!");
        }

        // 2. 判断 appid 参数是否存在
        String appid = pathVariables.get(APP_ID);
        if(appid == null) {
            throw new IllegalArgumentException("Request 中 appid 参数不存在!");
        }

        // 3. 判断是否配置了此 appid
        if (!this.wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
    }
}
