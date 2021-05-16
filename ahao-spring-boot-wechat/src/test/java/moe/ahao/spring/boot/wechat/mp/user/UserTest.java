package moe.ahao.spring.boot.wechat.mp.user;


import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxMpErrorMsgEnum;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.tag.WxTagListUser;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class UserTest extends BaseMpTest {

    /**
     * 设置用户备注名
     * @param openId 用户openId
     * @param remark 备注名
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140838">设置用户备注名</a>
     */
    @ParameterizedTest
    @MethodSource("updateRemark")
    void updateRemark(String openId, String remark) throws WxErrorException {
        userService.userUpdateRemark(openId, remark);
    }
    static Stream<Arguments> updateRemark() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "用户备注名")
        );
    }

    /**
     * 获取单个用户基本信息
     * @param openId 用户openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140839">获取用户基本信息</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"oXt6f5vHknuLmWiGfQTiUVj7X7U0"})
    void selectOne(String openId) throws WxErrorException {
        WxMpUser user = userService.userInfo(openId);
        Assertions.assertNotNull(user, "获取用户失败");
        System.out.println(user.toString());
    }

    /**
     * 获取多个用户基本信息, 最多支持一次拉取100条
     * @param openId 用户openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140839">获取用户基本信息 批量获取用户基本信息</a>
     */
    @ParameterizedTest
    @MethodSource("selectList")
    void selectList(List<String> openId) throws WxErrorException {
        Assertions.assertTrue(openId.size() <= 100, "最多支持一次拉取100条");

        List<WxMpUser> users = userService.userInfoList(openId);
        Assertions.assertNotNull(users, "获取用户列表失败");

        for (WxMpUser user : users) {
            System.out.println(user.toString());
            System.out.println("------------------------------------------");
        }
    }
    static Stream<Arguments> selectList() {
        return Stream.of(
            Arguments.arguments(Arrays.asList("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "oXt6f5vHknuLmWiGfQTiUVj7X7U0"))
        );
    }

    /**
     * 获取所有用户的 openId, 最多支持一次拉取 10000 条, 循环拉取即可
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140840">获取用户列表</a>
     */
    @Test
    void selectOpenIdList() throws WxErrorException {
        String nextOpenId = null;
        do {
            WxMpUserList userList = userService.userList(nextOpenId);
            System.out.println("关注总用户数:"+userList.getTotal());
            System.out.println("本次拉取用户数量:"+userList.getCount());
            System.out.println("本次拉取的用户openId:"+userList.getOpenids());
            System.out.println("-------------------------------------------------------");
            nextOpenId = userList.getNextOpenid();
        } while (StringUtils.isNotBlank(nextOpenId));
    }

    /**
     * 获取标签下的粉丝 openId
     * @param tagId 标签Id
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 获取标签下粉丝列表</a>
     */
    @ParameterizedTest
    @ValueSource(longs = {0,1,2,101})
    void selectOpenIdListByTagId(Long tagId) {
        String nextOpenId = null;
        do {
            try {
                WxTagListUser userList = tagService.tagListUser(tagId, nextOpenId);
                System.out.println("本次拉取该标签的粉丝数量:" + userList.getCount());
                WxTagListUser.WxTagListUserData data = userList.getData();
                if(data != null) {
                    System.out.println("本次拉取该标签的用户openId:" + data.getOpenidList());
                }
                System.out.println("-------------------------------------------------------");
                nextOpenId = userList.getNextOpenid();
            } catch (WxErrorException e) {
                WxError error = e.getError();
                switch (error.getErrorCode()) {
                    case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg());break;
                    case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                    case 45159: Assertions.fail("非法的tag_id"); break;
                }
                e.printStackTrace();
            }
        } while (StringUtils.isNotBlank(nextOpenId));
    }

    /**
     * 微信公众号主体变更后, 迁移旧公众号的用户到新公众号, openId 会被修改
     * 最多支持一次迁移 100 条
     * @param fromAppId 原公众号的 appid
     * @param toAppId   新公众号的 appid
     * @see <a href="http://kf.qq.com/faq/1901177NrqMr190117nqYJze.html">openid转换接口</a>
     */
    @ParameterizedTest
    @MethodSource("moveFansOpenId")
    void moveFansOpenId(String fromAppId, String toAppId) throws WxErrorException {
        String nextOpenId = null;
        do {
            // 1. 切换到旧公众号, 抓取最多 10000 条 openId
            if(!wxMpService.switchover(fromAppId)) {
                throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", fromAppId));
            }
            WxMpUserList userList = userService.userList(nextOpenId);
            List<String> openIdList = userList.getOpenids();
            System.out.println("关注总用户数:"+userList.getTotal());
            System.out.println("本次拉取用户数量:"+userList.getCount());
            System.out.println("本次拉取的用户openId:"+openIdList);

            // 2. 切换到新公众号, 每次只能迁移 100 条 openId
            if(!wxMpService.switchover(toAppId)) {
                throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", toAppId));
            }
            for (int i = 0, len = openIdList.size(); i < len; i+=100) {
                List<String> fromOpenIdList = openIdList.subList(i, Math.min(i + 100, len)); // 一次只能迁移100个
                userService.changeOpenid(fromAppId, fromOpenIdList);
            }
            System.out.println("-------------------------------------------------------");
            nextOpenId = userList.getNextOpenid();
        } while (StringUtils.isNotBlank(nextOpenId));
    }
    static Stream<Arguments> moveFansOpenId() {
        return Stream.of(
            Arguments.arguments("wxb4cdac81aa7fe3f2", "wxb4cdac81aa7fe3f2")
        );
    }


}
