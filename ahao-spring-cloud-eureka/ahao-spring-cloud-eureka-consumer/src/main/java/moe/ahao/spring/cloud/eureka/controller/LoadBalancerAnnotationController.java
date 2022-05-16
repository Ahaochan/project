package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.domain.entity.Result;
import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 使用 @LoadBalanced 做负载均衡
 */
@RestController
public class LoadBalancerAnnotationController {
    @Bean
    @LoadBalanced // 负载均衡配置
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/param1")
    public String param(@RequestParam String msg) {
        // http://服务名/请求路径
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true
        String serverName = EurekaConsumerApplication.serverName;
        return restTemplate.getForObject("http://" + serverName + "/param?msg=" + msg, String.class);
    }

    @PostMapping("/body1")
    public Result<Object> body(@RequestBody Result<Object> dto) {
        String serverName = EurekaConsumerApplication.serverName;
        return restTemplate.postForObject("http://" + serverName + "/body", dto, Result.class);
    }

    @PostMapping(value = "/form-data1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Object> formData(@RequestParam String param, @RequestParam String json, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = FilenameUtils.getExtension(fileName);
        String prefix = FilenameUtils.getBaseName(fileName);
        String serverName = EurekaConsumerApplication.serverName;
        String url = "http://" + serverName + "/form-data";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("param", param);
        map.add("json", json);
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        map.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Result> response = restTemplate.postForEntity(url, request, Result.class);

        FileUtils.deleteQuietly(file);
        return response.getBody();
    }

    @GetMapping(value = "/download1.txt")
    public Result<Object> download(@RequestParam String name, @RequestParam String data) throws IOException {
        String serverName = EurekaConsumerApplication.serverName;

        ResponseEntity<byte[]> entity = restTemplate.exchange("http://" + serverName + "/download.txt?name=" + name + "&data=" + data, HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()), byte[].class);
        byte[] body = entity.getBody();
        if (body == null) {
            return Result.failure("");
        }
        String msg = new String(body, StandardCharsets.UTF_8);
        return Result.success(name, msg);
    }
}
