package com.ahao.web.module.download;

import com.ahao.util.web.RequestHelper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

@Controller
public class DownloadController {

    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        // 1. 初始化响应体
        String msg = "中文+- 测试 chinese test 1234567";
        Resource body = new ByteArrayResource(msg.getBytes(StandardCharsets.UTF_8));

        // 2. 初始化文件名, 避免IE中文乱码等问题
        String filename = RequestHelper.safetyFilename("中文+- 测试.txt");

        // 3. 初始化响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
        headers.setContentLength(msg.length());

        // 4. 返回, 二进制文件则返回 byte[] 即可.
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
//        return new ResponseEntity<>(new ByteArrayOutputStream().toByteArray(), headers, HttpStatus.OK);
    }
}
