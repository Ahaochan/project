package moe.ahao.spring.boot.wechat.mp.user;

import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxMpErrorMsgEnum;
import me.chanjar.weixin.mp.bean.result.WxMpUserBlacklistGetResult;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

class BlacklistTest extends BaseMpTest {

    /**
     * 获取公众号的黑名单列表, 每次最多 10000 个 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA">黑名单管理 获取公众号的黑名单列表</a>
     */
    @Test
    void getOpenIdsByBlacklist() {
        String nextOpenId = null;
        do {
            try {
                WxMpUserBlacklistGetResult result = blackListService.getBlacklist(nextOpenId);
                System.out.println("总拉黑用户数量:"+result.getTotal());
                System.out.println("本次拉取用户数量:"+result.getCount());
                System.out.println("本次拉取的用户openId:"+result.getOpenidList());
                System.out.println("-------------------------------------------------------");
                if(Objects.equals(nextOpenId, result.getNextOpenid())) {
                    break;
                }
                nextOpenId = result.getNextOpenid();
            } catch (WxErrorException e) {
                WxError error = e.getError();
                switch (error.getErrorCode()) {
                    case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg());break;
                    case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                    case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
                }
                e.printStackTrace();
            }
        } while (StringUtils.isNotBlank(nextOpenId));
    }

    /**
     * 拉黑用户, 一次拉黑最多允许 20 个
     * @param openIds 用户 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA">黑名单管理 拉黑用户</a>
     */
    @ParameterizedTest
    @MethodSource("blacklist")
    void pushToBlacklist(String... openIds) {
        try {
            blackListService.pushToBlacklist(Arrays.asList(openIds));
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg());break;
                case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
                case 40032: Assertions.fail(WxMpErrorMsgEnum.CODE_40032.getMsg()); break;
            }
            e.printStackTrace();
        }
    }
    static Stream<Arguments> blacklist() {
        return Stream.of(
            Arguments.arguments((Object) new String[]{"oXt6f5vHknuLmWiGfQTiUVj7X7U0"})
        );
    }

    /**
     * 取消拉黑用户, 一次取消拉黑用户最多允许 20 个
     * @param openIds 用户 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA">黑名单管理 取消拉黑用户</a>
     */
    @ParameterizedTest
    @MethodSource("blacklist")
    void pullFromBlacklist(String... openIds) {
        try {
            blackListService.pullFromBlacklist(Arrays.asList(openIds));
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg());break;
                case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
                case 40032: Assertions.fail(WxMpErrorMsgEnum.CODE_40032.getMsg()); break;
            }
            e.printStackTrace();
        }
    }
}
