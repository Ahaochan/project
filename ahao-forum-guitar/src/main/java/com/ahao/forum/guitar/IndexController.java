package com.ahao.forum.guitar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/index", "/"})
    public String index(){
        return "index";
    }

    @GetMapping("/test")
    public String test(){
        return "admin/pane/pane-role";
    }
}
