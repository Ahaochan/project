package com.ahao.spring.boot.wechat.mp.kefu;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import com.ahao.spring.boot.wechat.mp.media.MediaPermCommonTest;
import com.ahao.spring.boot.wechat.mp.media.MediaPermNewsTest;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class KefuMessageTest extends BaseMpTest {

    /**
     * 发送文本消息, 支持插入跳小程序的文字链
     * @param openId  用户 openId
     * @param content 发送内容
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送文本消息</a>
     */
    @ParameterizedTest
    @MethodSource("sendText")
    void sendText(String openId, String content) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.TEXT()
            .toUser(openId)
            .content(content)
            .build();

        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送文本消息失败");
    }
    static Stream<Arguments> sendText() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "你好世界")
//            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "你好, <a href=\"http://www.qq.com\" data-miniprogram-appid=\"wx5f3a632e47edacd7\" data-miniprogram-path=\"pages/mall\">这是个小程序</a>")
        );
    }

    /**
     * 发送图片消息
     * @param openId  用户 openId
     * @param mediaId 上传到微信的图片素材id, {@link MediaPermCommonTest#uploadFile()}
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送图片消息</a>
     */
    @ParameterizedTest
    @MethodSource("sendImage")
    void sendImage(String openId, String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.IMAGE()
            .toUser(openId)
            .mediaId(mediaId)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送图片消息失败");
    }
    static Stream<Arguments> sendImage() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI")
        );
    }

    /**
     * 发送语音消息
     * @param openId  用户 openId
     * @param mediaId 上传到微信的语音素材id, {@link MediaPermCommonTest#uploadFile()}
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送语音消息</a>
     */
    @ParameterizedTest
    @MethodSource("sendVoice")
    void sendVoice(String openId, String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.VOICE()
            .toUser(openId)
            .mediaId(mediaId)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送语音消息失败");
    }
    static Stream<Arguments> sendVoice() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI")
        );
    }

    /**
     * 发送视频消息
     * @param openId       用户 openId
     * @param title        视频标题
     * @param mediaId      上传到微信的视频素材id, {@link MediaPermCommonTest#uploadFile()}
     * @param thumbMediaId 视频缩略图的图片素材id, {@link MediaPermCommonTest#uploadFile()}
     * @param description  视频描述
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送视频消息</a>
     */
    @ParameterizedTest
    @MethodSource("sendVideo")
    void sendVideo(String openId, String title, String mediaId, String thumbMediaId, String description) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.VIDEO()
            .toUser(openId)
            .title(title)
            .mediaId(mediaId)
            .thumbMediaId(thumbMediaId)
            .description(description)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送视频消息失败");
    }
    static Stream<Arguments> sendVideo() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "视频标题", "aXCEWuQAasl23bXTlZCnJ-olHOSibriz0IgFKUonKmU", null, "视频描述")
        );
    }

    /**
     * 发送音乐消息
     * @param openId       用户 openId
     * @param title        音乐标题
     * @param description  音乐描述
     * @param musicUrl     音乐链接
     * @param hqMusicUrl   高品质音乐链接, wifi环境优先使用该链接播放音乐
     * @param thumbMediaId 音乐缩略图的图片素材id, {@link MediaPermCommonTest#uploadFile()}
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送音乐消息</a>
     */
    @ParameterizedTest
    @MethodSource("sendMusic")
    void sendMusic(String openId, String title, String description, String musicUrl, String hqMusicUrl, String thumbMediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.MUSIC()
            .toUser(openId)
            .title(title)
            .description(description)
            .musicUrl(musicUrl)
            .hqMusicUrl(hqMusicUrl)
            .thumbMediaId(thumbMediaId)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送音乐消息失败");
    }
    static Stream<Arguments> sendMusic() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "音乐标题", "音乐描述", "https://i.y.qq.com/v8/playsong.html?songid=1811241", "https://i.y.qq.com/v8/playsong.html?songid=1811241", "aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI")
        );
    }

    /**
     * 发送图文消息(点击跳转到外链)
     * 图文消息条数限制在1条以内, 注意, 如果图文数超过1, 则将会返回错误码45008.
     * @param openId      用户 openId
     * @param title       图文标题
     * @param description 图文描述
     * @param picUrl      图文缩略图链接, 支持JPG、PNG格式, 较好的效果为大图640*320, 小图80*80
     * @param url         点击跳转链接
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送图文消息(点击跳转到外链)</a>
     */
    @ParameterizedTest
    @MethodSource("sendNews")
    void sendNews(String openId, String title, String description, String picUrl, String url) throws WxErrorException {
        WxMpKefuMessage.WxArticle article = new WxMpKefuMessage.WxArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setPicUrl(picUrl);
        article.setUrl(url);

        WxMpKefuMessage message = WxMpKefuMessage.NEWS()
            .toUser(openId)
            .addArticle(article)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送图文消息(点击跳转到外链)失败");
    }
    static Stream<Arguments> sendNews() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "图文标题", "图文描述", "https://www.baidu.com/img/bd_logo1.png", "https://www.baidu.com/")
        );
    }

    /**
     * 发送图文消息(点击跳转到图文消息页面)
     * 图文消息条数限制在1条以内, 注意, 如果图文数超过1, 则将会返回错误码45008.
     * @param openId  用户 openId
     * @param mediaId 上传到微信的图文素材id, {@link MediaPermNewsTest#uploadNews()}
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送图文消息(点击跳转到图文消息页面)</a>
     */
    @ParameterizedTest
    @MethodSource("sendMpNews")
    void sendMpNews(String openId, String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.MPNEWS()
            .toUser(openId)
            .mediaId(mediaId)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送图文消息(点击跳转到图文消息页面)失败");
    }
    static Stream<Arguments> sendMpNews() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "aXCEWuQAasl23bXTlZCnJ1x7ffVXAobLRABWQr2R9Ok")
        );
    }

    /**
     * 发送小程序卡片(要求小程序与公众号已关联)
     * @param openId       用户 openId
     * @param appId        小程序的 appId
     * @param pagePath     小程序的页面路径
     * @param title        小程序卡片标题
     * @param thumbMediaId 小程序卡片图片, 建议大小为520*416, 上传到微信的图片素材id, {@link MediaPermCommonTest#uploadFile()}
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547">发送小程序卡片</a>
     */
    @ParameterizedTest
    @MethodSource("sendMiniProgramPage")
    void sendMiniProgramPage(String openId, String appId, String pagePath, String title, String thumbMediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.MINIPROGRAMPAGE()
            .toUser(openId)
            .appId(appId)
            .pagePath(pagePath)
            .title(title)
            .thumbMediaId(thumbMediaId)
            .build();
        boolean flag = kefuService.sendKefuMessage(message);
        Assertions.assertTrue(flag, "发送小程序卡片失败");
    }
    static Stream<Arguments> sendMiniProgramPage() {
        return Stream.of(
            Arguments.arguments("oXt6f5vHknuLmWiGfQTiUVj7X7U0", "aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI")
        );
    }
}
