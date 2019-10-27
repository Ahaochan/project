package moe.ahao.spring.cloud.eureka.controller;

import com.ahao.domain.entity.AjaxDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public AjaxDTO body(@RequestBody AjaxDTO dto) {
        return dto;
    }

    @PostMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        logger.error("文件名: " + file.getName());
        logger.error("原始文件名: " + file.getOriginalFilename());
        logger.error("文件大小: " + file.getSize());
        List<String> result = Arrays.asList(param, json, new String(file.getBytes(), StandardCharsets.UTF_8));
        return AjaxDTO.success(result);
    }
}
