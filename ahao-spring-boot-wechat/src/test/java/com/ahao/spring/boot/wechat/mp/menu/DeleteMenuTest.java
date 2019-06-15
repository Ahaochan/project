package com.ahao.spring.boot.wechat.mp.menu;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DeleteMenuTest extends BaseMpTest {
    /**
     * 调用此接口会删除默认菜单及全部个性化菜单
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141015">自定义菜单删除接口</a>
     */
    @Test
    @Ignore("需配置实际的权限数据")
    public void deleteAllMenu() {
        try {
            menuService.menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * 删除指定 menuId 的个性化菜单
     * menuId 为菜单id，可以通过自定义菜单查询接口获取。
     * @see GetMenuTest#getMenu()
     * @see GetMenuTest#getAllMenu()
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296">删除个性化菜单</a>
     */
    @Test
    @Ignore("需配置实际的权限数据")
    public void deleteMenuById() {
        String menuId = "";
        try {
            menuService.menuDelete(menuId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
