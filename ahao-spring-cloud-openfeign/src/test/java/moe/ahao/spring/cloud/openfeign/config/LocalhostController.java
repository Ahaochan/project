package moe.ahao.spring.cloud.openfeign.config;

import com.ahao.domain.entity.AjaxDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 从 ahao-web 复制的 SimpleController
 */
@RestController
public class LocalhostController implements LocalhostFeignApi {
    @Autowired
    private LocalhostFeignApi api;

    @GetMapping("/path-{id}")
    public Integer path(@PathVariable Integer id) {
        return api.path(id);
    }

    @GetMapping("/get1")
    public AjaxDTO get1(Integer result, String msg) {
        return api.get1(result, msg);
    }

    @GetMapping("/get2")
    public AjaxDTO get2(@RequestParam Integer result, @RequestParam String msg) {
        return api.get2(result, msg);
    }

    @GetMapping("/get3")
    public AjaxDTO get3(AjaxDTO req) {
        return api.get3(req);
    }

    @PostMapping("/post1")
    public AjaxDTO post1(Integer result, String msg) {
        return api.post1(result, msg);
    }

    @PostMapping("/post2")
    public AjaxDTO post2(@RequestParam Integer result, @RequestParam String msg) {
        return api.post2(result, msg);
    }

    @PostMapping("/post3")
    public AjaxDTO post3(AjaxDTO req) {
        return api.post3(req);
    }

    @PostMapping("/post4")
    public AjaxDTO post4(String msg, @RequestBody AjaxDTO req) {
        return api.post4(msg, req);
    }

    @PostMapping("multipart1")
    public AjaxDTO multipart1(MultipartFile file) {
        return api.multipart1(file);
    }

    @PostMapping("multipart2")
    public AjaxDTO multipart2(@RequestParam MultipartFile file) {
        return api.multipart2(file);
    }

    @PostMapping("multipart3")
    public AjaxDTO multipart3(@RequestPart MultipartFile file) {
        return api.multipart3(file);
    }

    @PostMapping("multipart4")
    public AjaxDTO multipart4(AjaxDTO req, @RequestParam MultipartFile file) {
        return api.multipart4(req, file);
    }
    @PostMapping("multipart5")
    public AjaxDTO multipart5(@RequestPart AjaxDTO req, @RequestParam MultipartFile file) {
        return api.multipart5(req, file);
    }
    @PostMapping("multipart6")
    public AjaxDTO multipart6(@RequestPart AjaxDTO req, @RequestPart MultipartFile file) {
        return api.multipart6(req, file);
    }
}
