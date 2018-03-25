package com.ahao.forum.guitar.module.admin.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {


    @GetMapping("/admin/user")
    public String list(){
        return "admin/pane/pane-user";
    }

    @GetMapping("/admin/user-{userId}")
    public void userInfo(@PathVariable("userId") Integer userId){

    }
}
