package com.ahao.web.module;

import com.ahao.domain.entity.AjaxDTO;
import org.springframework.web.bind.annotation.*;

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
    public AjaxDTO get2(AjaxDTO req) {
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
    public AjaxDTO post3(AjaxDTO req) {
        return req;
    }

    @PostMapping("/post4")
    public AjaxDTO post4(String msg, @RequestBody AjaxDTO req) {
        return AjaxDTO.get(req.getResult(), req.getMsg() + msg, req.getObj());
    }
}
