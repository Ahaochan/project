package moe.ahao.spring.cloud.openfeign.config;

import moe.ahao.domain.entity.AjaxDTO;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public class LocalhostFeignFallback implements LocalhostFeignApi {

    @GetMapping("/path-{id}")
    public Integer path(@PathVariable Integer id) {
        return null;
    }

    @GetMapping("/get1")
    public AjaxDTO get1(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @GetMapping("/get2")
    public AjaxDTO get2(@RequestParam Integer result, @RequestParam String msg) {
        return AjaxDTO.failure();
    }

    @GetMapping("/get3")
    public AjaxDTO get3(@SpringQueryMap AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @PostMapping("/post1")
    public AjaxDTO post1(Integer result, String msg) {
        return AjaxDTO.failure();
    }

    @PostMapping("/post2")
    public AjaxDTO post2(@RequestParam Integer result, @RequestParam String msg) {
        return AjaxDTO.failure();
    }

    @PostMapping("/post3")
    public AjaxDTO post3(@SpringQueryMap AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @PostMapping("/post4")
    public AjaxDTO post4(@RequestParam String msg, @RequestBody AjaxDTO req) {
        return AjaxDTO.failure();
    }

    @PostMapping("multipart1")
    public AjaxDTO multipart1(MultipartFile file) {
        return AjaxDTO.failure();
    }

    @PostMapping("multipart2")
    public AjaxDTO multipart2(@RequestParam MultipartFile file) {
        return AjaxDTO.failure();
    }

    @PostMapping("multipart3")
    public AjaxDTO multipart3(@RequestPart MultipartFile file) {
        return AjaxDTO.failure();
    }

    @PostMapping("multipart4")
    public AjaxDTO multipart4(AjaxDTO req, @RequestParam MultipartFile file) {
        return AjaxDTO.failure();
    }
    @PostMapping("multipart5")
    public AjaxDTO multipart5(@RequestPart AjaxDTO req, @RequestParam MultipartFile file) {
        return AjaxDTO.failure();
    }

    @Override
    public AjaxDTO fallback() {
        return AjaxDTO.failure();
    }
//    @PostMapping("multipart6")
//    public AjaxDTO multipart6(@RequestPart AjaxDTO req, @RequestPart MultipartFile file) throws IOException {
//        return AjaxDTO.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
//    }
}
