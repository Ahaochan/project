package moe.ahao.spring.cloud.openfeign.config;

import com.ahao.domain.entity.AjaxDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "localhost", url = "https://127.0.0.1", path = "/simple", fallback = LocalhostFeignFallback.class)
public interface LocalhostFeignApi {
    @GetMapping("/path-{id}")
    Integer path(@PathVariable Integer id);

    @GetMapping("/get1")
    AjaxDTO get1(Integer result, String msg);

    @GetMapping("/get2")
    AjaxDTO get2(@RequestParam Integer result, @RequestParam String msg);

    @GetMapping("/get3")
    AjaxDTO get3(AjaxDTO req);

    @PostMapping("/post1")
    AjaxDTO post1(Integer result, String msg);

    @PostMapping("/post2")
    AjaxDTO post2(@RequestParam Integer result, @RequestParam String msg);

    @PostMapping("/post3")
    AjaxDTO post3(AjaxDTO req);

    @PostMapping("/post4")
    AjaxDTO post4(String msg, @RequestBody AjaxDTO req);

    @PostMapping("/multipart1")
    AjaxDTO multipart1(MultipartFile file);

    @PostMapping("/multipart2")
    AjaxDTO multipart2(@RequestParam MultipartFile file);

    @PostMapping("/multipart3")
    AjaxDTO multipart3(@RequestPart MultipartFile file);

    @PostMapping("/multipart4")
    AjaxDTO multipart4(AjaxDTO req, @RequestParam MultipartFile file);

    @PostMapping("/multipart5")
    AjaxDTO multipart5(@RequestPart AjaxDTO req, @RequestParam MultipartFile file);

    @PostMapping("/multipart6")
    AjaxDTO multipart6(@RequestPart AjaxDTO req, @RequestPart MultipartFile file);

    @PostMapping("/fallback")
    AjaxDTO fallback();
}
