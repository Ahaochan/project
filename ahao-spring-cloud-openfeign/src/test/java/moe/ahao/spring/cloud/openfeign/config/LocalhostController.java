package moe.ahao.spring.cloud.openfeign.config;

import moe.ahao.domain.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 从 ahao-web 复制的 SimpleController
 */
@RestController
public class LocalhostController {
    @Autowired
    private LocalhostFeignApi api;

    @GetMapping("/path-{id}")
    public Integer path(@PathVariable Integer id) {
        return api.path(id);
    }

    @GetMapping("/get2")
    public Result<Object> get2(@RequestParam Integer code, @RequestParam String msg) {
        return api.get2(code, msg);
    }

    @GetMapping("/get3")
    public Result<Object> get3(@SpringQueryMap Result<Object> req) {
        return api.get3(req);
    }

    @PostMapping("/post2")
    public Result<Object> post2(@RequestParam Integer code, @RequestParam String msg) {
        return api.post2(code, msg);
    }

    @PostMapping("/post3")
    public Result<Object> post3(@SpringQueryMap Result<Object> req) {
        return api.post3(req);
    }

    @PostMapping("/post4")
    public Result<Object> post4(@RequestParam String msg, @RequestBody Result<Object> req) {
        return api.post4(msg, req);
    }

    @PostMapping("/multipart1")
    public Result<Object> multipart1(MultipartFile file) {
        return api.multipart1(file);
    }

    @PostMapping("/multipart2")
    public Result<Object> multipart2(@RequestParam MultipartFile file) {
        return api.multipart2(file);
    }

    @PostMapping("/multipart3")
    public Result<Object> multipart3(@RequestPart MultipartFile file) {
        return api.multipart3(file);
    }

    @PostMapping("/multipart4")
    public Result<Object> multipart4(Result<Object> req, @RequestParam MultipartFile file) {
        return api.multipart4(req, file);
    }
    @PostMapping("/multipart5")
    public Result<Object> multipart5(@RequestPart Result<Object> req, @RequestParam MultipartFile file) {
        return api.multipart5(req, file);
    }
    // @PostMapping("/multipart6")
    // public Result<Object> multipart6(@RequestPart Result<Object> req, @RequestPart MultipartFile file) {
    //    return api.multipart6(req, file);
    // }

    @GetMapping("/fallback")
    public Result<Object> fallback() {
        return api.fallback();
    }
}
