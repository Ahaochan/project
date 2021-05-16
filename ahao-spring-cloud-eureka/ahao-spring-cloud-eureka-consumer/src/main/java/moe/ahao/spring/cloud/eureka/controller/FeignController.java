package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import moe.ahao.util.commons.io.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FeignController {
    private static final String serverName = EurekaConsumerApplication.serverName;

    // 注意要打开启动类的 @EnableFeignClients 注解
    @FeignClient(value = serverName) // 由客户端配置文件中的 spring.application.name 配置
    public interface SimpleFeignClient {
        // 需要 spring-cloud-starter-feign 依赖
        @GetMapping("/param")
        String param(@RequestParam String msg);

        @PostMapping("/body")
        AjaxDTO body(@RequestBody AjaxDTO dto);

        @PostMapping(value = "/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            // 注意, 这里 body 的 required = false 并不生效
        AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart(value = "file", required = false) MultipartFile file);

        @GetMapping("/download.txt")
        ResponseEntity<Resource> download(@RequestParam String name, @RequestParam String data);
    }

    @Autowired
    private SimpleFeignClient feignClient;

    @GetMapping("/param3")
    public String param(@RequestParam String msg) {
        return feignClient.param(msg);
    }

    @PostMapping("/body3")
    public AjaxDTO body(@RequestBody AjaxDTO dto) {
        return feignClient.body(dto);
    }

    @PostMapping(value = "/form-data3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart(value = "file", required = false) MultipartFile file) {
        return feignClient.formData(param, json, file);
    }

    @GetMapping(value = "/download3.txt")
    public AjaxDTO download(@RequestParam String name, @RequestParam String data) throws IOException {
        ResponseEntity<Resource> download = feignClient.download(name, data);
        Resource body = download.getBody();
        if (body == null) {
            return AjaxDTO.failure("");
        }
        String filename = body.getFilename();
        String response = IOHelper.toString(body.getInputStream());
        return AjaxDTO.success(filename, response);
    }
}
