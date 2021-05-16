package moe.ahao.spring.boot.wechat.mp.menu;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

class GetMenuTest extends BaseMpTest {
    /**
     * 查询通过 API 设置的菜单
     * @see CreateMenuTest
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141014">自定义菜单查询接口</a>
     */
    @Test
    void getMenu() {
        try {
            WxMpMenu wxMpMenu = menuService.menuGet();
            Assertions.assertNotNull(wxMpMenu);
            System.out.println(wxMpMenu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    /**
     * 查询通过 API 设置的菜单 和 通过公众平台设置的菜单
     * @see CreateMenuTest
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1434698695">获取自定义菜单配置接口</a>
     */
    @Test
    void getAllMenu() {
        try {
            WxMpGetSelfMenuInfoResult selfMenuInfo = menuService.getSelfMenuInfo();
            Assertions.assertNotNull(selfMenuInfo);
            System.out.println(selfMenuInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    /**
     * 通过 userId 查看特定群体能看到的菜单
     * userId 可以是粉丝的 OpenID, 也可以是粉丝的微信号
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296">测试个性化菜单匹配结果</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    void getMenuByUserId(String userId) {
        Assumptions.assumeTrue(StringUtils.isNotBlank(userId), "需配置OpenID或微信号");
        try {
            WxMenu menu = menuService.menuTryMatch(userId);
            Assertions.assertNotNull(menu);
            System.out.println(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
