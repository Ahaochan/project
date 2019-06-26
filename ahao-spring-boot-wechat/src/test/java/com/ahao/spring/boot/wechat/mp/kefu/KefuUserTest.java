package com.ahao.spring.boot.wechat.mp.kefu;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfInfo;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.stream.Stream;

/**
 * 请注意，必须先在公众平台官网为公众号设置微信号后才能使用该能力
 */
class KefuUserTest extends BaseMpTest {
    static Stream<Arguments> kefuUser() {
        return Stream.of(
            Arguments.arguments("Ahaochan", "客服1号", "pw1")
        );
    }

    /**
     * 每个公众号最多添加10个客服账号
     * @param account  完整客服账号, 格式为: 账号前缀@公众号微信号
     * @param nickname 客服昵称, 最长6个汉字或12个英文字符
     * @param password 客服账号登录密码, 格式为密码明文的32位加密MD5值. 该密码仅用于在公众平台官网的多客服功能中使用, 若不使用多客服功能, 则不必设置密码.
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">客服帐号管理 添加客服帐号</a>
     */
    @ParameterizedTest
    @MethodSource("kefuUser")
    void addKefuUser(String account, String nickname, String password) throws WxErrorException {
        WxMpKfAccountRequest request = new WxMpKfAccountRequest(account, nickname, password);

        boolean flag = kefuService.kfAccountAdd(request);
        Assertions.assertTrue(flag, "添加客服帐号失败");
    }

    /**
     * 获取所有客服账号
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">客服帐号管理 获取所有客服账号</a>
     */
    @Test
    void selectKefuList() throws WxErrorException {
        WxMpKfList list = kefuService.kfList();
        if(list == null) {
            return;
        }

        for (WxMpKfInfo wxMpKfInfo : list.getKfList()) {
            System.out.println("客服账号: " + wxMpKfInfo.getAccount());
            System.out.println("客服头像: " + wxMpKfInfo.getHeadImgUrl());
            System.out.println("客服工号: " + wxMpKfInfo.getId());
            System.out.println("客服昵称: " + wxMpKfInfo.getNick());
            System.out.println("客服微信号: " + wxMpKfInfo.getWxAccount());
            System.out.println("客服在线状态: " + wxMpKfInfo.getStatus());
            System.out.println("客服会话人数: " + wxMpKfInfo.getAcceptedCase());
            System.out.println("绑定邀请的微信号: " + wxMpKfInfo.getInviteWx());
            System.out.println("绑定邀请过期时间: " + wxMpKfInfo.getInviteExpireTime());
            System.out.println("绑定邀请状态: " + wxMpKfInfo.getInviteStatus());
        }
    }

    /**
     * 修改客服账号
     * @param account  完整客服账号, 格式为: 账号前缀@公众号微信号
     * @param nickname 客服昵称, 最长6个汉字或12个英文字符
     * @param password 客服账号登录密码, 格式为密码明文的32位加密MD5值. 该密码仅用于在公众平台官网的多客服功能中使用, 若不使用多客服功能, 则不必设置密码.
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">客服帐号管理 修改客服帐号</a>
     */
    @ParameterizedTest
    @MethodSource("kefuUser")
    void updateKefuUser(String account, String nickname, String password) throws WxErrorException {
        WxMpKfAccountRequest request = new WxMpKfAccountRequest(account, nickname, password);

        boolean flag = kefuService.kfAccountUpdate(request);
        Assertions.assertTrue(flag, "修改客服帐号失败");
    }

    /**
     * 设置客服帐号的头像
     * @param account  完整客服账号, 格式为: 账号前缀@公众号微信号
     * @param filepath 头像文件路径
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">客服帐号管理 设置客服帐号的头像</a>
     */
    @ParameterizedTest
    @MethodSource("updateKefuUserHeadImg")
    void updateKefuUserHeadImg(String account, String filepath) throws WxErrorException {
        boolean flag = kefuService.kfAccountUploadHeadImg(account, new File(filepath));
        Assertions.assertTrue(flag, "设置客服帐号的头像失败");
    }
    static Stream<Arguments> updateKefuUserHeadImg() {
        return Stream.of(
            Arguments.arguments("Ahaochan", "D:\\123.png")
        );
    }

    /**
     * 删除客服帐号
     * @param account 完整客服账号, 格式为: 账号前缀@公众号微信号
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">客服帐号管理 删除客服帐号</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"Ahaochan"})
    void deleteKefuUser(String account) throws WxErrorException {
        boolean flag = kefuService.kfAccountDel(account);
        Assertions.assertTrue(flag, "删除客服帐号失败");
    }
}
