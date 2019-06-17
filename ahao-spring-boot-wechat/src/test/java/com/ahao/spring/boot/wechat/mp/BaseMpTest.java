package com.ahao.spring.boot.wechat.mp;

import com.ahao.spring.boot.Starter;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class BaseMpTest {

    @Autowired
    protected WxMpService wxMpService;

    protected WxMpMenuService menuService;

    @BeforeEach
    public void init() throws WxErrorException {
        Assumptions.assumeTrue(StringUtils.isNotBlank(wxMpService.getWxMpConfigStorage().getAppId()), "需配置实际的微信配置");
        menuService = wxMpService.getMenuService();
    }
}
