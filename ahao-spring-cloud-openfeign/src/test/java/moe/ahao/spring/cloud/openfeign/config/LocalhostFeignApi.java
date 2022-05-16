package moe.ahao.spring.cloud.openfeign.config;

import moe.ahao.domain.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "localhost", url = "http://127.0.0.1:8080", path = "/simple", fallback = LocalhostFeignFallback.class)
public interface LocalhostFeignApi {
    @GetMapping("/path-{id}")
    Integer path(@PathVariable Integer id);

    @GetMapping("/get2")
    Result<Object> get2(@RequestParam Integer result, @RequestParam String msg);

    @GetMapping("/get3")
    Result<Object> get3(@SpringQueryMap Result<Object> req);

    @PostMapping("/post2")
    Result<Object> post2(@RequestParam Integer result, @RequestParam String msg);

    @PostMapping("/post3")
    Result<Object> post3(@SpringQueryMap Result<Object> req);

    @PostMapping("/post4")
    Result<Object> post4(@RequestParam String msg, @RequestBody Result<Object> req);

    @PostMapping(value = "/multipart1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<Object> multipart1(MultipartFile file);

    @PostMapping(value = "/multipart2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<Object> multipart2(@RequestParam MultipartFile file);

    @PostMapping(value = "/multipart3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<Object> multipart3(@RequestPart MultipartFile file);

    @PostMapping("/multipart4")
    Result<Object> multipart4(Result<Object> req, @RequestParam MultipartFile file);

    @PostMapping("/multipart5")
    Result<Object> multipart5(@RequestPart Result<Object> req, @RequestParam MultipartFile file);

    // @PostMapping("/multipart6")
    // Result<Object> multipart6(@RequestPart Result<Object> req, @RequestPart MultipartFile file);

    @PostMapping("/fallback")
    Result<Object> fallback();
}
