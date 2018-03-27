package com.ahao.forum.guitar.module.admin.user.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.admin.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserApiController {

    private UserService userService;

    @Autowired
    public UserApiController(UserService userService){
        this.userService = userService;
    }


    @RequiresAuthentication
    @PostMapping("/modify/password")
    public AjaxDTO modifyPassword(@RequestParam String oldPassword,
                                  @RequestParam String newPassword){
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        AjaxDTO ajaxDTO = userService.modifyPassword(userData.getLong("id"), oldPassword, newPassword);
        return ajaxDTO;
    }

    @GetMapping("/admin/user")
    public AjaxDTO getUser(@RequestParam("page") Integer page,
                           @RequestParam("search") String search){
        int pageSize = 10;
        return null;
    }
}
