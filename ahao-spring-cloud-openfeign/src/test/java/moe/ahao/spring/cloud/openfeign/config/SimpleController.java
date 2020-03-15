package moe.ahao.spring.cloud.openfeign.config;

import com.ahao.domain.entity.AjaxDTO;
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
    public AjaxDTO get1(Integer result, String msg) {
        return AjaxDTO.get(result, msg, null);
    }

    @GetMapping("/get2")
    public AjaxDTO get2(@RequestParam Integer result, @RequestParam String msg) {
        return AjaxDTO.get(result, msg, null);
    }

    @GetMapping("/get3")
    public AjaxDTO get3(@SpringQueryMap AjaxDTO req) {
        return req;
    }

    @PostMapping("/post1")
    public AjaxDTO post1(Integer result, String msg) {
        return AjaxDTO.get(result, msg, null);
    }

    @PostMapping("/post2")
    public AjaxDTO post2(@RequestParam Integer result, @RequestParam String msg) {
        return AjaxDTO.get(result, msg, null);
    }

    @PostMapping("/post3")
    public AjaxDTO post3(@SpringQueryMap AjaxDTO req) {
        return req;
    }

    @PostMapping("/post4")
    public AjaxDTO post4(@RequestParam String msg, @RequestBody AjaxDTO req) {
        return AjaxDTO.get(req.getResult(), req.getMsg() + msg, req.getObj());
    }

    @PostMapping("multipart1")
    public AjaxDTO multipart1(MultipartFile file) throws IOException {
        return AjaxDTO.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart2")
    public AjaxDTO multipart2(@RequestParam MultipartFile file) throws IOException {
        return AjaxDTO.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart3")
    public AjaxDTO multipart3(@RequestPart MultipartFile file) throws IOException {
        return AjaxDTO.success(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @PostMapping("multipart4")
    public AjaxDTO multipart4(AjaxDTO req, @RequestParam MultipartFile file) throws IOException {
        return AjaxDTO.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
    }
    @PostMapping("multipart5")
    public AjaxDTO multipart5(@RequestPart AjaxDTO req, @RequestParam MultipartFile file) throws IOException {
        return AjaxDTO.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
    }
//    @PostMapping("multipart6")
//    public AjaxDTO multipart6(@RequestPart AjaxDTO req, @RequestPart MultipartFile file) throws IOException {
//        return AjaxDTO.success(req.getMsg() + new String(file.getBytes(), StandardCharsets.UTF_8));
//    }
}
