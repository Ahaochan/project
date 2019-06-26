package com.ahao.spring.boot.wechat.mp.service;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WxMpConfigStorageServiceInMemoryImpl implements WxMpConfigStorageService {
    @Override
    public List<WxMpConfigStorage> selectList() {
        List<WxMpConfigStorage> result = new ArrayList<>();

        WxMpInMemoryConfigStorage config1 = new WxMpInMemoryConfigStorage();
        config1.setAppId("123");
        config1.setSecret("456");
        config1.setToken("789");
        config1.setAesKey("aes");
        result.add(config1);

        return result;
    }
}
