package com.ahao.forum.guitar.manager.rbac.shiro.controller;

import com.ahao.commons.entity.AjaxDTO;
import com.ahao.forum.guitar.manager.rbac.shiro.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private RegisterService registerService;
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register")
    public String register(){
        return "manager/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxDTO register(@RequestParam String username, @RequestParam String password) {
        boolean success = registerService.register(username, password);
        if(success){
            return AjaxDTO.success("注册成功! 请登录!");
        }
        return AjaxDTO.failure("注册失败! 请联系管理员!");
    }

    @PostMapping("/register/checkUserName")
    @ResponseBody
    public AjaxDTO checkUserName(@RequestParam String username){
        boolean isExists = registerService.isExistUsername(username);
        if(isExists){
            return AjaxDTO.failure("用户名已注册!");
        }
        return AjaxDTO.success("该用户名暂未注册");
    }
}
