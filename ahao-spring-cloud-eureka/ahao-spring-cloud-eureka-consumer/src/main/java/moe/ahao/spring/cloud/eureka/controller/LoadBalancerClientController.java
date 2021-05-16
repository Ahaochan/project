package moe.ahao.spring.cloud.eureka.controller;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.spring.cloud.eureka.EurekaConsumerApplication;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
 * 使用 LoadBalancerClient 做负载均衡
 */
@RestController
public class LoadBalancerClientController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/param2")
    public String param(@RequestParam String msg) {
        // 服务名   由客户端配置文件中的 spring.application.name 配置
        // 请求路径 由@RequestMapping配置
        // 跑这个 demo 需要在 application.yml 中把 eureka.client.register-with-eureka 暂时设置为true
        String serverName = EurekaConsumerApplication.serverName;
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://" + server.getHost() + ":" + server.getPort() + "/param?msg=" + msg, String.class);
    }

    @PostMapping("/body2")
    public AjaxDTO body(@RequestBody AjaxDTO dto) {
        String serverName = EurekaConsumerApplication.serverName;
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://" + server.getHost() + ":" + server.getPort() + "/body", dto, AjaxDTO.class);
    }

    @PostMapping(value = "/form-data2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxDTO formData(@RequestParam String param, @RequestParam String json, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = FilenameUtils.getExtension(fileName);
        String prefix = FilenameUtils.getBaseName(fileName);
        String serverName = EurekaConsumerApplication.serverName;
        ServiceInstance server = loadBalancerClient.choose(serverName);
        String url = "http://" + server.getHost() + ":" + server.getPort() + "/form-data";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("param", param);
        map.add("json", json);
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        map.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AjaxDTO> response = restTemplate.postForEntity(url, request, AjaxDTO.class);

        FileUtils.deleteQuietly(file);
        return response.getBody();
    }

    @GetMapping(value = "/download2.txt")
    public AjaxDTO download(@RequestParam String name, @RequestParam String data) throws IOException {
        String serverName = EurekaConsumerApplication.serverName;
        ServiceInstance server = loadBalancerClient.choose(serverName);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> entity = restTemplate.exchange("http://" + server.getHost() + ":" + server.getPort() + "/download.txt?name=" + name + "&data=" + data, HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()), byte[].class);
        byte[] body = entity.getBody();
        if (body == null) {
            return AjaxDTO.failure("");
        }
        String msg = new String(body, StandardCharsets.UTF_8);
        return AjaxDTO.success(name, msg);
    }
}
