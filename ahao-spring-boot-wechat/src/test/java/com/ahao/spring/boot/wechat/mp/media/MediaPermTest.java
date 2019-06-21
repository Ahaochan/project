package com.ahao.spring.boot.wechat.mp.media;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialVideoInfoResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.InputStream;

class MediaPermTest extends BaseMpTest {

    /**
     * 上传图文永久素材
     * @param author          图文消息的作者
     * @param thumbMediaId    图文消息的封面图片素材id(必须是永久mediaID)
     * @param title           图文消息的标题
     * @param content         内容
     * @param sourceUrl       阅读原文链接
     * @param showCoverPic    是否显示封面
     * @param digest          描述
     * @param openComment     是否开放评论
     * @param onlyFansComment 是否只能粉丝评论
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729">新增永久素材</a>
     */
    @ParameterizedTest
    @CsvSource({"Ahaochan,aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI,标题,内容,http://www.baidu.com,true,描述,true,false"})
    void uploadNews(String author, String thumbMediaId, String title, String content, String sourceUrl,
                    boolean showCoverPic, String digest, boolean openComment, boolean onlyFansComment) throws WxErrorException {
        // 单图文消息的例子
        WxMpMaterialNews news = new WxMpMaterialNews();

        WxMpMaterialNews.WxMpMaterialNewsArticle article1 = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article1.setAuthor(author);
        article1.setThumbMediaId(thumbMediaId);
        article1.setTitle(title);
        article1.setContent(content);
        article1.setContentSourceUrl(sourceUrl);
        article1.setShowCoverPic(showCoverPic);
        article1.setDigest(digest);
        article1.setNeedOpenComment(openComment);
        article1.setOnlyFansCanComment(onlyFansComment);

        news.addArticle(article1);

        WxMpMaterialUploadResult result = materialService.materialNewsUpload(news);
        System.out.println("图文消息链接: " + result.getUrl());
        System.out.println("图文消息素材的media_id: " + result.getMediaId());
        System.out.println("错误码: " + result.getErrCode());
        System.out.println("错误信息:" + result.getErrMsg());
    }

    /**
     * 获取 {@link #uploadNews(String, String, String, String, String, boolean, String, boolean, boolean)} (String, String)} 上传的永久图文素材
     * @param mediaId 永久图文素材标识
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738730">获取永久素材</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"aXCEWuQAasl23bXTlZCnJ1x7ffVXAobLRABWQr2R9Ok"})
    void downloadNews(String mediaId) throws WxErrorException {
        WxMpMaterialNews news = materialService.materialNewsInfo(mediaId);

        for (WxMpMaterialNews.WxMpMaterialNewsArticle article : news.getArticles()) {
            System.out.println(article.toString());
            System.out.println("--------------------------------------------");
        }
    }

    /**
     * 上传非图文永久素材
     * @param name      素材名称
     * @param mediaType 文件类型{@link WxConsts.MediaFileType}
     * @param filepath  上传文件的所在路径
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729">新增永久素材</a>
     */
    @ParameterizedTest
    @CsvSource({"素材名.png,image,D:\\123.png"})
    void uploadFile(String name, String mediaType, String filepath) throws WxErrorException {
        WxMpMaterial wxMaterial = new WxMpMaterial();
        wxMaterial.setFile(new File(filepath));
        wxMaterial.setName(name);
        WxMpMaterialUploadResult result = materialService.materialFileUpload(mediaType, wxMaterial);

        System.out.println("文件链接: " + result.getUrl());
        System.out.println("文件素材的media_id: " + result.getMediaId());
        System.out.println("错误码: " + result.getErrCode());
        System.out.println("错误信息:" + result.getErrMsg());
    }

    /**
     * 获取 {@link #uploadFile(String, String, String)} (String, String)} 上传的永久图片或声音素材
     * @param mediaId  永久图片或声音素材
     * @param filepath 保存到文件路径下
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738730">获取永久素材</a>
     */
    @ParameterizedTest
    @CsvSource({"aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI,D:\\123.png"})
    void downloadImageOrVoice(String mediaId, String filepath) throws Exception {
        InputStream is = materialService.materialImageOrVoiceDownload(mediaId);

        File file = new File(filepath);
        FileUtils.copyInputStreamToFile(is, file);

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
    }

    /**
     * 上传视频永久素材
     * @param name         素材名称
     * @param filepath     上传文件的所在路径
     * @param title        视频标题
     * @param introduction 视频简介
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729">新增永久素材</a>
     */
    @ParameterizedTest
    @CsvSource({"素材名.mp4,D:\\123.mp4,视频标题,视频简介"})
    void uploadVideo(String name, String filepath, String title, String introduction) throws WxErrorException {
        String extension = FilenameUtils.getExtension(name);
        Assertions.assertTrue(StringUtils.isNotBlank(extension), "name需要包含文件后缀, 否则微信服务器返回错误码");

        WxMpMaterial wxMaterial = new WxMpMaterial();
        wxMaterial.setFile(new File(filepath));
        wxMaterial.setName(name);
        wxMaterial.setVideoTitle(title);
        wxMaterial.setVideoIntroduction(introduction);
        WxMpMaterialUploadResult result = materialService.materialFileUpload(WxConsts.MediaFileType.VIDEO, wxMaterial);

        System.out.println("视频链接: " + result.getUrl());
        System.out.println("视频素材的media_id: " + result.getMediaId());
        System.out.println("错误码: " + result.getErrCode());
        System.out.println("错误信息:" + result.getErrMsg());
    }

    /**
     * 获取 {@link #uploadVideo(String, String, String, String)} 上传的永久视频素材
     * @param mediaId  永久视频素材
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738730">获取永久素材</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"aXCEWuQAasl23bXTlZCnJ-olHOSibriz0IgFKUonKmU"})
    void downloadVideo(String mediaId) throws WxErrorException {
        WxMpMaterialVideoInfoResult video = materialService.materialVideoInfo(mediaId);

        System.out.println("视频标题: "+video.getTitle());
        System.out.println("视频描述: "+video.getDescription());
        System.out.println("视频地址: "+video.getDownUrl());
        System.out.println("---------------------------------------------------");
        System.out.println(video.toString());
    }
}
