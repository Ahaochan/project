package com.ahao.forum.guitar.module.upload.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.util.lang.StringHelper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    ServletContext context;

    @PostMapping("upload/img")
    @ResponseBody
    public AjaxDTO fileUpload(@RequestParam MultipartFile file,
                              @RequestParam String filePath) throws IOException {
        // 原始名称
        String oldFileName = file.getOriginalFilename(); // 获取上传文件的原名
        // 存储图片的虚拟本地路径（这里需要配置tomcat的web模块路径，双击猫进行配置）
        String path = context.getRealPath(File.separator + "img" + File.separator + filePath);
        File pathDir = new File(path);
        // 上传图片
        if(StringHelper.isEmpty(oldFileName)){
            return AjaxDTO.failure("文件为空");
        }


        // 新的图片名称
        String newFileName = UUID.randomUUID() + oldFileName;
        // 新图片
        File newFile = new File(pathDir, newFileName);
        // 将内存中的数据写入磁盘
        FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        // 将新图片名称返回到前端
        JSONObject json = new JSONObject();
        json.put("url", "/img/"+filePath+"/"+newFileName);

        return AjaxDTO.success("上传成功", json);

    }
}
