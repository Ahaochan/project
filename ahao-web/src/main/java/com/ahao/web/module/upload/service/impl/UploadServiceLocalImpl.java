package com.ahao.web.module.upload.service.impl;


import com.ahao.exception.BizException;
import com.ahao.web.module.upload.config.properties.UploadProperties;
import com.ahao.web.module.upload.model.UploadDTO;
import com.ahao.web.module.upload.service.UploadService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UploadServiceLocalImpl implements UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadServiceLocalImpl.class);

    private UploadProperties uploadProperties;
    public UploadServiceLocalImpl(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public UploadDTO base64img(String picture) {
        // 1. 判断 base 64 是否合法
        if(StringUtils.isEmpty(picture)){
            throw BizException.create(HttpStatus.BAD_REQUEST, "非法的base64参数");
        }
        String[] metaData = StringUtils.splitByWholeSeparator(picture, "base64,");
        if(metaData == null || metaData.length != 2) {
            throw BizException.create(HttpStatus.BAD_REQUEST, "非法的base64参数");
        }

        // 2. 获取文件格式
        String base64Type = metaData[0].toLowerCase();
        String base64 = metaData[1];
        String suffix;
        switch (base64Type) {
            case "data:image/jpeg;":   suffix = ".jpg"; break;
            case "data:image/x-icon;": suffix = ".ico"; break;
            case "data:image/gif;":    suffix = ".gif"; break;
            case "data:image/png;":    suffix = ".png"; break;
            default:  throw BizException.create(HttpStatus.BAD_REQUEST, "上传格式错误!");
        }

        // 3. 生成文件名, 并写入数据
        String filename = DigestUtils.md5Hex(picture) + suffix;
        String fileSavePath   = uploadProperties.getFileSaveUri();
        String filePrefixPath = uploadProperties.getFilePrefixUri();
        File file = new File(fileSavePath, filename);

        UploadDTO result = UploadDTO.create(filePrefixPath + filename, filename);
        if (file.exists()) {
            return result;
        }
        byte[] bytes = Base64.decodeBase64(base64);
        try (FileOutputStream os = new FileOutputStream(file)) {
            IOUtils.write(bytes, os);
        } catch (IOException e) {
            logger.error("上传Base64图片错误!", e);
            throw BizException.create(HttpStatus.INTERNAL_SERVER_ERROR, "上传base64图片错误!");
        }
        return result;
    }

    @Override
    public UploadDTO file(MultipartFile upload) {
        String originalFilename = upload.getOriginalFilename();
        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(originalFilename);
        String fileSavePath   = uploadProperties.getFileSaveUri();
        String filePrefixPath = uploadProperties.getFilePrefixUri();

        File file = new File(fileSavePath, filename);
        try (FileOutputStream os = new FileOutputStream(file)) {
            IOUtils.copy(upload.getInputStream(), os);
            return UploadDTO.create(filePrefixPath + filename, originalFilename);
        } catch (IOException e) {
            logger.error("上传文件失败:", e);
            throw BizException.create(HttpStatus.INTERNAL_SERVER_ERROR, "上传文件失败!");
        }
    }
}
