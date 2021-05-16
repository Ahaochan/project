package moe.ahao.spring.boot.wechat.mp;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.*;
import moe.ahao.spring.boot.Starter;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
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

    protected WxMpKefuService kefuService;
    protected WxMpMaterialService materialService;
    protected WxMpMenuService menuService;
    protected WxMpUserService userService;
    protected WxMpUserTagService tagService;
    protected WxMpQrcodeService qrCodeService;
    protected WxMpCardService cardService;
    protected WxMpStoreService storeService;
    protected WxMpDataCubeService dataCubeService;
    protected WxMpUserBlacklistService blackListService;
    protected WxMpTemplateMsgService templateMsgService;
    protected WxMpSubscribeMsgService subscribeMsgService;
    protected WxMpDeviceService deviceService;
    protected WxMpShakeService shakeService;
    protected WxMpMemberCardService memberCardService;
    protected WxMpMassMessageService massMessageService;
    protected WxMpAiOpenService aiOpenService;
    protected WxMpWifiService wifiService;
    protected WxMpMarketingService marketingService;

    @BeforeEach
    public void init() throws WxErrorException {
        Assumptions.assumeTrue(false, "微信公众号的单元测试依赖实际的公众号配置");
        Assumptions.assumeTrue(StringUtils.isNotBlank(wxMpService.getWxMpConfigStorage().getAppId()), "需配置实际的微信配置");

        Map<String, WxMpConfigStorage> configStorageMap = ReflectHelper.getValue(wxMpService, "configStorageMap");
        configStorageMap.forEach((k, v) -> System.out.println("加载微信公众配置:" + v.toString()));
        Assertions.assertEquals(1, configStorageMap.size(), "单元测试公众号测试只能配置一个公众号");


        kefuService = wxMpService.getKefuService();
        materialService = wxMpService.getMaterialService();
        menuService = wxMpService.getMenuService();
        userService = wxMpService.getUserService();
        tagService = wxMpService.getUserTagService();
        qrCodeService = wxMpService.getQrcodeService();
        cardService = wxMpService.getCardService();
        storeService = wxMpService.getStoreService();
        dataCubeService = wxMpService.getDataCubeService();
        blackListService = wxMpService.getBlackListService();
    }
}
