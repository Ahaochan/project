package moe.ahao.spring.boot.wechat.mp.menu;

import me.chanjar.weixin.common.error.WxErrorException;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

class DeleteMenuTest extends BaseMpTest {
    /**
     * 调用此接口会删除默认菜单及全部个性化菜单
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141015">自定义菜单删除接口</a>
     */
    @Test
    void deleteAllMenu() {
        try {
            menuService.menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    /**
     * 删除指定 menuId 的个性化菜单
     * menuId 为菜单id，可以通过自定义菜单查询接口获取。
     * @see GetMenuTest#getMenu()
     * @see GetMenuTest#getAllMenu()
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296">删除个性化菜单</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    void deleteMenuById(String menuId) {
        Assumptions.assumeTrue(StringUtils.isNotBlank(menuId), "需配置实际的权限数据");
        try {
            menuService.menuDelete(menuId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
