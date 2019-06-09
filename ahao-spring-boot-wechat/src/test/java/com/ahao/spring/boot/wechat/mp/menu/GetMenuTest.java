package com.ahao.spring.boot.wechat.mp.menu;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.junit.Assert;
import org.junit.Test;

public class GetMenuTest extends BaseMpTest {
    /**
     * 查询通过 API 设置的菜单
     * @see CreateMenuTest
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141014">自定义菜单查询接口</a>
     */
    @Test
    public void getMenu() {
        try {
            WxMpMenu wxMpMenu = menuService.menuGet();
            Assert.assertNotNull(wxMpMenu);
            System.out.println(wxMpMenu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * 查询通过 API 设置的菜单 和 通过公众平台设置的菜单
     * @see CreateMenuTest
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1434698695">获取自定义菜单配置接口</a>
     */
    @Test
    public void getAllMenu() {
        try {
            WxMpGetSelfMenuInfoResult selfMenuInfo = menuService.getSelfMenuInfo();
            Assert.assertNotNull(selfMenuInfo);
            System.out.println(selfMenuInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * 通过 userId 查看特定群体能看到的菜单
     * userId 可以是粉丝的 OpenID, 也可以是粉丝的微信号
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296">测试个性化菜单匹配结果</a>
     */
    @Test
    public void getMenuByUserId() {
        String userId = "";
        try {
            WxMenu menu = menuService.menuTryMatch(userId);
            Assert.assertNotNull(menu);
            System.out.println(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
