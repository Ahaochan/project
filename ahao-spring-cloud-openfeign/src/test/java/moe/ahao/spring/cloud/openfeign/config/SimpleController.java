package moe.ahao.spring.cloud.openfeign.config;

import moe.ahao.domain.entity.Result;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/simple")
public class SimpleController {

    @GetMapping("/path-{id}")
    public Integer path(@PathVariable Integer id) {
        return id;
    }

    @GetMapping("/get1")
    public Result<Object> get1(Integer result, String msg) {
        return Result.get(result, msg, null);
    }

    @GetMapping("/get2")
    public Result<Object> get2(@RequestParam Integer result, @RequestParam String msg) {
        return Result.get(result, msg, null);
    }

    @GetMapping("/get3")
    public Result<Object> get3(@SpringQueryMap Result<Object> req) {
        return req;
    }

    @PostMapping("/post1")
    public Result<Object> post1(Integer result, String msg) {
        return Result.get(result, msg, null);
    }

    @PostMapping("/post2")
    public Result<Object> post2(@RequestParam Integer result, @RequestParam String msg) {
        return Result.get(result, msg, null);
    }

    @PostMapping("/post3")
    public Result<Object> post3(@SpringQueryMap Result<Object> req) {
        return req;
    }

    @PostMapping("/post4")
    public Result<Object> post4(@RequestParam String msg, @RequestBody Result<Object> req) {
        return Result.get(req.getCode(), req.getMsg() + msg, req.getObj());
    }

    @PostMapping("multipart1")
    public Result<Object> multipart1(MultipartFile file) throws IOException {
        return Result.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart2")
    public Result<Object> multipart2(@RequestParam MultipartFile file) throws IOException {
        return Result.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart3")
    public Result<Object> multipart3(@RequestPart MultipartFile file) throws IOException {
        return Result.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart4")
    public Result<Object> multipart4(Result<Object> req, @RequestParam MultipartFile file) throws IOException {
        return Result.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
    }
    @PostMapping("multipart5")
    public Result<Object> multipart5(@RequestPart Result<Object> req, @RequestParam MultipartFile file) throws IOException {
        return Result.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
    }
    // @PostMapping("multipart6")
    // public Result<Object> multipart6(@RequestPart Result<Object> req, @RequestPart MultipartFile file) throws IOException {
    //    return Result.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
    // }
}
