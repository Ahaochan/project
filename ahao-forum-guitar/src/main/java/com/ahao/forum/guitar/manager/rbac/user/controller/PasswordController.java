package com.ahao.forum.guitar.manager.rbac.user.controller;

import com.ahao.commons.entity.AjaxDTO;
import com.ahao.commons.entity.IDataSet;
import com.ahao.forum.guitar.manager.rbac.user.service.PasswordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class PasswordController {

    private PasswordService passwordService;
    public PasswordController(PasswordService passwordService){
        this.passwordService = passwordService;
    }

    @GetMapping("/password")
    public String modifyPassword() {
        return "manager/password/manager-password";
    }


    @PostMapping("/api/password/modify")
    @ResponseBody
    public AjaxDTO modifyPassword(@RequestParam String oldPassword,
                                  @RequestParam String newPassword){
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        String errorMsg = passwordService.modifyPassword(userData.getLong("id"), oldPassword, newPassword);
        if(StringUtils.isNotEmpty(errorMsg)){
            return AjaxDTO.failure(errorMsg);
        }
        return AjaxDTO.success("密码修改成功");
    }

}
