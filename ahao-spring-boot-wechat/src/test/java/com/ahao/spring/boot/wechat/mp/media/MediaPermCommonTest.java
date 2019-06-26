package com.ahao.spring.boot.wechat.mp.media;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialCountResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * 永久非图文素材单元测试
 * 常见操作包括
 * 1. 上传 {@link #uploadFile(String, String, String)}
 * 2. 下载图片或声音 {@link #downloadImageOrVoice(String, String)}
 * 3. 下载视频 {@link MediaPermVideoTest#downloadVideo(String)}
 * 4. 查询 {@link #selectFileByPage(String, int, int)}
 * 5. 删除 {@link #delete(String)}
 */
public class MediaPermCommonTest extends BaseMpTest {
    /**
     * 上传永久非图文素材
     * @param name      素材名称
     * @param mediaType 文件类型{@link WxConsts.MediaFileType}
     * @param filepath  上传文件的所在路径
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729">新增永久素材</a>
     */
    @ParameterizedTest
    @MethodSource("uploadFile")
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
    public static Stream<Arguments> uploadFile() {
        return Stream.of(
            Arguments.arguments("素材名.png", WxConsts.MediaFileType.IMAGE, "D:\\123.png")
        );
    }

    /**
     * 获取 {@link #uploadFile(String, String, String)} (String, String)} 上传的永久图片或声音素材
     * @param mediaId  永久图片或声音素材
     * @param filepath 保存到文件路径下
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738730">获取永久素材</a>
     */
    @ParameterizedTest
    @MethodSource("downloadImageOrVoice")
    void downloadImageOrVoice(String mediaId, String filepath) throws Exception {
        InputStream is = materialService.materialImageOrVoiceDownload(mediaId);

        File file = new File(filepath);
        FileUtils.copyInputStreamToFile(is, file);

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
    }
    static Stream<Arguments> downloadImageOrVoice() {
        return Stream.of(
            Arguments.arguments("aXCEWuQAasl23bXTlZCnJ66Tuv677XLGkkDnJozdjtI", "D:\\123.png")
        );
    }

    @ParameterizedTest
    @MethodSource("selectFileByPage")
    void selectFileByPage(String type, int page, int pageSize) throws WxErrorException {
        WxMpMaterialCountResult count = materialService.materialCount();
        System.out.println("永久图文素材数量: " + count.getNewsCount());
        System.out.println("永久图片素材数量: " + count.getImageCount());
        System.out.println("永久声音素材数量: " + count.getVoiceCount());
        System.out.println("永久视频素材数量: " + count.getVideoCount());
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");

        int offset = (page - 1) * pageSize;
        WxMpMaterialFileBatchGetResult result = materialService.materialFileBatchGet(type, offset, pageSize);
        System.out.println("永久" + type + "素材数量: " + result.getTotalCount());
        switch (type) {
            case WxConsts.MediaFileType.VIDEO: Assertions.assertEquals(result.getTotalCount(), count.getVideoCount(), type + "素材总数不一致");break;
            case WxConsts.MediaFileType.IMAGE: Assertions.assertEquals(result.getTotalCount(), count.getImageCount(), type + "素材总数不一致");break;
            case WxConsts.MediaFileType.VOICE: Assertions.assertEquals(result.getTotalCount(), count.getVoiceCount(), type + "素材总数不一致");break;
            default: Assertions.fail("未知素材类型:" + type);
        }


        System.out.println("本次获取素材数量: " + result.getItemCount());

        for (WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem item : result.getItems()) {
            System.out.println(item.toString());
            System.out.println("--------------------------------------------------------------------------");
        }
    }
    static Stream<Arguments> selectFileByPage() {
        return Stream.of(
            Arguments.arguments(WxConsts.MediaFileType.VIDEO, 1, 20),
            Arguments.arguments(WxConsts.MediaFileType.IMAGE, 1, 20),
            Arguments.arguments(WxConsts.MediaFileType.VOICE, 1, 20)
        );
    }

    /**
     * 删除永久素材
     * @param mediaId 永久素材标识
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738731">删除永久素材</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"aXCEWuQAasl23bXTlZCnJ-olHOSibriz0IgFKUonKmU"})
    void delete(String mediaId) throws WxErrorException {
        boolean _true = materialService.materialDelete(mediaId);
        Assertions.assertTrue(_true, "目前接口只会返回true或抛出异常");

        // download FAIL
    }
}
