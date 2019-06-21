package com.ahao.spring.boot.wechat.mp.media;

import com.ahao.spring.boot.wechat.mp.BaseMpTest;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialVideoInfoResult;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.util.stream.Stream;

/**
 * 永久视频素材单元测试
 * 常见操作包括
 * 1. 上传 {@link #uploadVideo(String, String, String, String)}
 * 2. 下载 {@link #downloadVideo(String)}
 * 3. 查询 {@link MediaPermCommonTest#selectFileByPage(String, int, int)}
 * 4. 删除 {@link MediaPermCommonTest#delete(String)}
 */
class MediaPermVideoTest extends BaseMpTest {
    /**
     * 上传视频永久素材
     * @param name         素材名称
     * @param filepath     上传文件的所在路径
     * @param title        视频标题
     * @param introduction 视频简介
     * @see <a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729">新增永久素材</a>
     */
    @ParameterizedTest
    @MethodSource("uploadVideo")
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
    static Stream<Arguments> uploadVideo() {
        return Stream.of(
            Arguments.arguments("素材名.mp4", "D:\\123.mp4", "视频标题", "视频简介")
        );
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
