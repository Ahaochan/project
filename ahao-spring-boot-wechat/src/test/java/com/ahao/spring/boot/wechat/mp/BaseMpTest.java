package com.ahao.spring.boot.wechat.mp;

import com.ahao.spring.boot.Starter;
import com.ahao.util.commons.lang.ReflectHelper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class BaseMpTest {

    @Autowired
    protected WxMpService wxMpService;

    protected WxMpMenuService menuService;
    protected WxMpKefuService kefuService;
    protected WxMpUserService userService;
    protected WxMpMaterialService materialService;

    @BeforeEach
    public void init() throws WxErrorException {
        Assumptions.assumeTrue(StringUtils.isNotBlank(wxMpService.getWxMpConfigStorage().getAppId()), "需配置实际的微信配置");

        Map<String, WxMpConfigStorage> configStorageMap = ReflectHelper.getValue(wxMpService, "configStorageMap");
        configStorageMap.forEach((k,v) -> System.out.println("加载微信公众配置:"+v.toString()));
        Assertions.assertEquals(1, configStorageMap.size(), "单元测试公众号测试只能配置一个公众号");

        menuService = wxMpService.getMenuService();
        kefuService = wxMpService.getKefuService();
        userService = wxMpService.getUserService();
        materialService = wxMpService.getMaterialService();
    }
}
