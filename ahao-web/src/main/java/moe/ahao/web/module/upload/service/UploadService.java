package moe.ahao.web.module.upload.service;

import moe.ahao.web.module.upload.model.UploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传 base64 格式的图片
     * @param picture base64字符串, 如: data:image/png;base64,iVBORw0...5CYII=
     */
    UploadDTO base64img(String picture);

    /**
     * 上传 input[type="file"] 的文件
     * @param upload 文件
     */
    UploadDTO file(MultipartFile upload);
}
