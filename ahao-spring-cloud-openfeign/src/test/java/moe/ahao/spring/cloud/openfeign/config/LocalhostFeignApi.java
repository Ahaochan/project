package moe.ahao.spring.cloud.openfeign.config;

import com.ahao.domain.entity.AjaxDTO;
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
    AjaxDTO get2(@RequestParam Integer result, @RequestParam String msg);

    @GetMapping("/get3")
    AjaxDTO get3(@SpringQueryMap AjaxDTO req);

    @PostMapping("/post2")
    AjaxDTO post2(@RequestParam Integer result, @RequestParam String msg);

    @PostMapping("/post3")
    AjaxDTO post3(@SpringQueryMap AjaxDTO req);

    @PostMapping("/post4")
    AjaxDTO post4(@RequestParam String msg, @RequestBody AjaxDTO req);

    @PostMapping(value = "/multipart1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxDTO multipart1(MultipartFile file);

    @PostMapping(value = "/multipart2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxDTO multipart2(@RequestParam MultipartFile file);

    @PostMapping(value = "/multipart3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxDTO multipart3(@RequestPart MultipartFile file);

    @PostMapping("/multipart4")
    AjaxDTO multipart4(AjaxDTO req, @RequestParam MultipartFile file);

    @PostMapping("/multipart5")
    AjaxDTO multipart5(@RequestPart AjaxDTO req, @RequestParam MultipartFile file);

//    @PostMapping("/multipart6")
//    AjaxDTO multipart6(@RequestPart AjaxDTO req, @RequestPart MultipartFile file);

    @PostMapping("/fallback")
    AjaxDTO fallback();
}
