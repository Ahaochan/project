package moe.ahao.spring.cloud.eureka.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class ProviderController {
    @GetMapping("/no-args")
    public String noArgs() {
        return "无参方法请求成功";
    }

    @RequestMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String formData(@RequestParam String filename, @RequestPart("file") MultipartFile file) throws IOException {
        return "文件名:" + filename + ", 文件大小:" + file.getSize() + ", 文件内容:" + new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}
