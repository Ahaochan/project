package moe.ahao.spring.boot.wechat.mp.media;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import moe.ahao.spring.boot.wechat.mp.BaseMpTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

/**
 * 临时素材单元测试
 * 常见操作包括
 * 1. 上传 {@link #upload(String, String)}
 * 2. 下载 {@link #download(String, String)}
 */
class MediaTempTest extends BaseMpTest {

    /**
     * 上传有效期为 3 天的临时素材
     * @param mediaFileType 文件类型{@link WxConsts.MediaFileType}
     * @param filepath      上传文件的所在路径
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726">新增临时素材</a>
     */
    @ParameterizedTest
    @MethodSource("upload")
    void upload(String mediaFileType, String filepath) throws WxErrorException {
        File file = new File(filepath);

//        WxMediaUploadResult result = materialService.mediaUpload(WxConsts.MediaFileType.IMAGE, file);
        WxMediaUploadResult result = materialService.mediaUpload(mediaFileType, file);

        System.out.println("媒体文件类型: " + result.getType());
        System.out.println("上传时间戳: " + result.getCreatedAt());
        System.out.println("媒体文件标识: " + result.getMediaId());
        System.out.println("视频消息缩略图标识:" + result.getThumbMediaId());
    }
    static Stream<Arguments> upload() {
        return Stream.of(
            Arguments.arguments(WxConsts.MediaFileType.IMAGE, "D:\\123.png")
        );
    }


    /**
     * 获取 {@link #upload(String, String)} 上传的临时素材
     * @param mediaId  	媒体文件标识
     * @param filepath  输出文件的路径
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738727">获取临时素材</a>
     */
    @ParameterizedTest
    @MethodSource("download")
    void download(String mediaId, String filepath) throws Exception {
        File file = materialService.mediaDownload(mediaId);

        FileUtils.copyFile(file, new File(filepath));

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.length() > 0);
    }
    static Stream<Arguments> download() {
        return Stream.of(
            Arguments.arguments("fbcMU8-_RNb2ins3AwdBIswrzNNkpTKC5YYoWVeY8IpACsWXfM3d6o9tf_WV9mz2", "D:\\123.png")
        );
    }
}
