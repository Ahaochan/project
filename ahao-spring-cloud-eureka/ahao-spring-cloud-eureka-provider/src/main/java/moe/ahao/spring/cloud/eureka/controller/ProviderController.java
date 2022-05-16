package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.domain.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
public class ProviderController {
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @GetMapping("/param")
    public String param(@RequestParam String msg) {
        return msg;
    }

    @PostMapping("/body")
    public Result<Object> body(@RequestBody Result<Object> dto) {
        return dto;
    }

    @PostMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<List<String>> formData(@RequestParam String param, @RequestParam String json, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        logger.error("文件名: {}", file.getName());
        logger.error("原始文件名: {}", file.getOriginalFilename());
        logger.error("文件大小: {}", file.getSize());
        List<String> result = Arrays.asList(param, json, new String(file.getBytes(), StandardCharsets.UTF_8));
        return Result.success(result);
    }

    @GetMapping("/download.txt")
    public ResponseEntity<Resource> download(@RequestParam String name, @RequestParam String data) {
        // 1. 初始化响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment; filename=" + name);

        // 2. 生成响应体
        byte[] buf = data.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        Resource body = new InputStreamResource(bais);

        // 3. 返回
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
