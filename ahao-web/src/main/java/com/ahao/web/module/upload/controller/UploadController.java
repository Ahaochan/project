package com.ahao.web.module.upload.controller;

import com.ahao.util.commons.io.JSONHelper;
import com.ahao.web.module.upload.model.UploadDTO;
import com.ahao.web.module.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    /**
     * 上传 base64 格式的图片
     * @param picture base64字符串, 如: data:image/png;base64,iVBORw0...5CYII=
     */
    @PostMapping("/base64img")
    public UploadDTO base64img(@RequestParam String picture) {
        return uploadService.base64img(picture);
    }

    /**
     * 上传 input[name="file" type="file"] 的文件
     * @param upload 文件
     */
    @PostMapping("/file")
    public UploadDTO file(@RequestParam("file") MultipartFile upload) {
        return uploadService.file(upload);
    }

    /**
     * 上传 input[type="file"] 的文件, 兼容 IE8 及 IE8以下
     * 设置 produces = text/json;charset=UTF-8, 避免 IE 将 JSON 自动下载的问题
     * @param upload 文件
     */
    @PostMapping(value = "/ie/file", produces = "text/json;charset=UTF-8")
    public String ieFile(@RequestParam("file") MultipartFile upload) {
        return JSONHelper.toString(uploadService.file(upload));
    }
}
