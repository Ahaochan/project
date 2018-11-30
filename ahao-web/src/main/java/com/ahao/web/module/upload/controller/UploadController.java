package com.ahao.web.module.upload.controller;

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
}
