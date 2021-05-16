package moe.ahao.spring.boot.wechat.mp.user;

import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxMpErrorMsgEnum;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

class UserTagTest extends BaseMpTest {

    /**
     * 创建用户标签, 一个公众号, 最多可以创建 100 个标签
     * @param tagName 标签名
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 创建标签</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"测试标签"})
    void createTag(String tagName) {
        try {
            WxUserTag tag = tagService.tagCreate(tagName);
            System.out.println("------------------------------------------------------");
            System.out.println("标签Id:" + tag.getId());
            System.out.println("标签名称:" + tag.getName());
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 45157: Assertions.fail("标签名非法，请注意不能和其他标签重名"); break;
                case 45158: Assertions.fail("标签名长度超过30个字节"); break;
                case 45056: Assertions.fail("创建的标签数过多，请注意不能超过100个"); break;
            }
            e.printStackTrace();
        }
    }

    /**
     * 获取公众号已创建的标签
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 获取公众号已创建的标签</a>
     */
    @Test
    void selectTagList() throws WxErrorException {
        List<WxUserTag> tagList = tagService.tagGet();
        for (WxUserTag tag : tagList) {
            System.out.println("------------------------------------------------------");
            System.out.println("标签Id:" + tag.getId());
            System.out.println("标签名称:" + tag.getName());
            System.out.println("标签下的粉丝数" + tag.getCount());
        }
    }

    /**
     * 获取某个用户的标签
     * @param openId 用户的 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 获取用户身上的标签列表</a>
     */
    @ParameterizedTest
    @ValueSource(strings = "oXt6f5vHknuLmWiGfQTiUVj7X7U0")
    void selectTagListByOpenId(String openId) {
        try {
            List<Long> tagIdList = tagService.userTagList(openId);
            System.out.println("该用户下的TagId:" + tagIdList);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
            }
            e.printStackTrace();
        }
    }

    /**
     * 编辑标签, 重命名
     * @param tagId   要重命名的标签Id
     * @param tagName 新标签名称
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 编辑标签</a>
     */
    @ParameterizedTest
    @MethodSource("updateTagNameById")
    void updateTagNameById(Long tagId, String tagName) {
        try {
            Boolean _true = tagService.tagUpdate(tagId, tagName);
            Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 45157: Assertions.fail("标签名非法，请注意不能和其他标签重名"); break;
                case 45158: Assertions.fail("标签名长度超过30个字节"); break;
                case 45058: Assertions.fail("不能修改0/1/2这三个系统默认保留的标签"); break;
            }
            e.printStackTrace();
        }
    }
    static Stream<Arguments> updateTagNameById() {
        return Stream.of(
            Arguments.arguments(100L, "新标签名")
        );
    }

    /**
     * 删除粉丝数在 10w 以下的标签, 如果超过 10w, 则需要先解绑标签
     * @param tagId 要删除的标签Id
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 删除标签</a>
     */
    @ParameterizedTest
    @ValueSource(longs = {0,1,2,3,100})
    void deleteTag(Long tagId) {
        try {
            Boolean _true = tagService.tagDelete(tagId);
            Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 45058: Assertions.fail("不能修改0/1/2这三个系统默认保留的标签"); break;
                case 45057: Assertions.fail("该标签下粉丝数超过10w，不允许直接删除"); break;
            }
            e.printStackTrace();
        }
    }

    /**
     * 为多个 openId 粉丝绑定标签, 一个用户最多有 20 个标签
     * @param tagId 要绑定的标签 TagId
     * @param openIds 要绑定的用户 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 批量为用户打标签</a>
     */
    @ParameterizedTest
    @MethodSource("bindTag")
    void bindTag(Long tagId, String... openIds) {
        try {
            boolean _true = tagService.batchTagging(tagId, openIds);
            Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 40032: Assertions.fail(WxMpErrorMsgEnum.CODE_40032.getMsg()); break;
                case 45159: Assertions.fail("非法的标签"); break;
                case 45059: Assertions.fail("有粉丝身上的标签数已经超过限制，即超过20个"); break;
                case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
            }
            e.printStackTrace();
        }
    }
    static Stream<Arguments> bindTag() {
        return Stream.of(
            Arguments.arguments(101L, new String[]{"oXt6f5vHknuLmWiGfQTiUVj7X7U0"})
        );
    }

    /**
     * 为多个 openId 粉丝解绑标签
     * @param tagId 要绑定的标签 TagId
     * @param openIds 要绑定的用户 openId
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140837">用户标签管理 标签管理 批量为用户取消标签</a>
     */
    @ParameterizedTest
    @MethodSource("bindTag")
    void unbindTag(Long tagId, String... openIds) {
        try {
            boolean _true = tagService.batchUntagging(tagId, openIds);
            Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");
        } catch (WxErrorException e) {
            WxError error = e.getError();
            switch (error.getErrorCode()) {
                case -1: Assertions.fail(WxMpErrorMsgEnum.CODE_1.getMsg()); break;
                case 40032: Assertions.fail(WxMpErrorMsgEnum.CODE_40032.getMsg()); break;
                case 45159: Assertions.fail("非法的标签"); break;
                case 40003: Assertions.fail(WxMpErrorMsgEnum.CODE_40003.getMsg()); break;
                case 49003: Assertions.fail("传入的openid不属于此AppID"); break;
            }
            e.printStackTrace();
        }
    }
}
