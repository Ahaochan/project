package moe.ahao.spring.boot.wechat.mp.service;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;

import java.util.List;

public interface WxMpConfigStorageService {

    List<WxMpConfigStorage> selectList();
}
