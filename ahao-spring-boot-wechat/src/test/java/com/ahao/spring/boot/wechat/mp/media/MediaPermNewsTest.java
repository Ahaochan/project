package com.ahao.spring.boot.wechat.mp.media;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 永久图文素材单元测试
 * 常见操作包括
 * 1. 上传 {@link #uploadNews(String, String, String, String, String, boolean, String, boolean, boolean)}
 * 2. 下载 {@link #downloadNews(String)}
 * 3. 更新 {@link #uploadNews(String, String, String, String, String, boolean, String, boolean, boolean)}
 * 4. 查询 {@link #selectNewsByPage(int, int)}
 * 5. 删除 {@link MediaPermCommonTest#delete(String)}
 */
class MediaPermNewsTest extends BaseMpTest {

    /**
     * 上传永久图文素材
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
     * 修改永久图文素材
     * @param mediaId         要修改的永久图文素材标识
     * @param index           如果是多图文, 则选择要修改第几篇, 从 0 开始
     * @param author          图文消息的作者
     * @param thumbMediaId    图文消息的封面图片素材id(必须是永久mediaID)
     * @param title           图文消息的标题
     * @param content         内容
     * @param sourceUrl       阅读原文链接
     * @param showCoverPic    是否显示封面
     * @param digest          描述
     * @param openComment     是否开放评论
     * @param onlyFansComment 是否只能粉丝评论
     */
    @ParameterizedTest
    @CsvSource({"aXCEWuQAasl23bXTlZCnJ1x7ffVXAobLRABWQr2R9Ok,0," +
        "Ahaochan,aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI,新标题,新内容,http://www.baidu.com,true,新描述,true,false"})
    void modifyNews(String mediaId, int index,
                    String author, String thumbMediaId, String title, String content, String sourceUrl,
                    boolean showCoverPic, String digest, boolean openComment, boolean onlyFansComment) throws WxErrorException {
        WxMpMaterialArticleUpdate update = new WxMpMaterialArticleUpdate();

        update.setMediaId(mediaId);
        update.setIndex(index);

        WxMpMaterialNews.WxMpMaterialNewsArticle article = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article.setAuthor(author);
        article.setThumbMediaId(thumbMediaId);
        article.setTitle(title);
        article.setContent(content);
        article.setContentSourceUrl(sourceUrl);
        article.setShowCoverPic(showCoverPic);
        article.setDigest(digest);
        article.setNeedOpenComment(openComment);
        article.setOnlyFansCanComment(onlyFansComment);
        update.setArticles(article);

        boolean _true = materialService.materialNewsUpdate(update);
        Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");

        // download
    }

    @ParameterizedTest
    @CsvSource({"1,20"})
    void selectNewsByPage(int page, int pageSize) throws WxErrorException {
        WxMpMaterialCountResult count = materialService.materialCount();
        System.out.println("永久图文素材数量: " + count.getNewsCount());
        System.out.println("永久图片素材数量: " + count.getImageCount());
        System.out.println("永久声音素材数量: " + count.getVoiceCount());
        System.out.println("永久视频素材数量: " + count.getVideoCount());
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");

        int offset = (page - 1) * pageSize;
        WxMpMaterialNewsBatchGetResult result = materialService.materialNewsBatchGet(offset, pageSize);
        System.out.println("永久图文素材数量: " + result.getTotalCount());
        Assertions.assertEquals(result.getTotalCount(), count.getNewsCount(), "图文素材总数不一致");
        System.out.println("本次获取素材数量: " + result.getItemCount());

        for (WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem item : result.getItems()) {
            System.out.println(item.toString());
            System.out.println("--------------------------------------------------------------------------");
        }
    }
}
