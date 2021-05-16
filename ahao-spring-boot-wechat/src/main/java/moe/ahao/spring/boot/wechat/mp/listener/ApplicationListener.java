package moe.ahao.spring.boot.wechat.mp.listener;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import moe.ahao.spring.boot.wechat.mp.service.WxMpConfigStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationListener {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpConfigStorageService wxMpConfigStorageService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadWxMpConfigFromDB() {
        List<WxMpConfigStorage> configs = wxMpConfigStorageService.selectList();
        for (WxMpConfigStorage config : configs) {
            wxMpService.addConfigStorage(config.getAppId(), config);
        }
    }
}
