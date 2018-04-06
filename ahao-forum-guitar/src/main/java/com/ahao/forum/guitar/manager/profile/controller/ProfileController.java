package com.ahao.forum.guitar.manager.profile.controller;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.manager.profile.service.ProfileService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ProfileController {

    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequiresAuthentication
    @GetMapping("/profile")
    public String profile(Model model) {
        // 1. 获取用户信息
        long userId = ShiroHelper.getMyUserId();
        model.addAttribute("user", ShiroHelper.getCurrentUser());

        // 2. 获取用户资料
        IDataSet profile = profileService.getProfile(userId);
        model.addAttribute("profile", profile);

        // 3. 获取角色资料
        List<IDataSet> roles = profileService.getSelectedRole(userId);
        model.addAttribute("roles", roles);

        // 3. 获取权限资料
        List<IDataSet> auths = profileService.getSelectedAuth(userId);
        model.addAttribute("auths", auths);

        return "manager/profile/manager-profile";
    }

}
