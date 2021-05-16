package moe.ahao.spring.boot.wechat.mp.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import moe.ahao.spring.boot.wechat.mp.handler.LocationHandler;
import moe.ahao.spring.boot.wechat.mp.handler.LogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static me.chanjar.weixin.common.api.WxConsts.EventType.LOCATION;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfig {
    @Autowired
    private LogHandler logHandler;
//    private NullHandler nullHandler;
//    private KfSessionHandler kfSessionHandler;
//    private StoreCheckNotifyHandler storeCheckNotifyHandler;
    @Autowired
    private LocationHandler locationHandler;
//    private MenuHandler menuHandler;
//    private MsgHandler msgHandler;
//    private UnsubscribeHandler unsubscribeHandler;
//    private SubscribeHandler subscribeHandler;
//    private ScanHandler scanHandler;

    @Autowired
    private WxMpProperties properties;

    @Bean
    public WxMpService wxMpService() {
        List<WxMpInMemoryConfigStorage> configs = this.properties.getProperties();
        Assert.notEmpty(configs, "必须配置最少一个公众号");

        Map<String, WxMpConfigStorage> configStorageMap = configs
            .stream()
            .collect(toMap(WxMpInMemoryConfigStorage::getAppId, a -> a));

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configStorageMap);
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();
//
//        // 门店审核事件
//        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();
//
//        // 自定义菜单事件
//        newRouter.rule().async(false).msgType(EVENT).event(CLICK).handler(this.menuHandler).end();
//
//        // 点击菜单连接事件
//        newRouter.rule().async(false).msgType(EVENT).event(VIEW).handler(this.nullHandler).end();
//
//        // 关注事件
//        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
//
//        // 取消关注事件
//        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
//
        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(LOCATION).handler(this.locationHandler).end();
//
//        // 接收地理位置消息
////        newRouter.rule().async(false).msgType(LOCATION).handler(this.locationHandler).end();
//
//        // 扫码事件
////        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
//
//        // 默认
//        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

}
